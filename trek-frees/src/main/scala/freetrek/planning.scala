package freetrek.frees.planning

import freetrek.domain.{Expedition, Encounter, WarpSpeed, Dilithium, PhaserFrequency, DeflectorPolarity}
import freetrek.domain.{EmptySpace, Nebula, HostileSpaceCrab, AsteroidField, SpaceTimeAnomaly}
import PhaserFrequency._
import DeflectorPolarity._
import freetrek.frees.algebras._
import freestyle._
import freestyle.implicits._
import cats._
import cats.implicits._
import cats.free._
import cats.mtl._

trait MonadPlan[F[_]]
    extends ApplicativeLocal[F, PlanConfig]
    with MonadState[F, PlanState]

object MonadPlan {
    def apply[F[_]: MonadPlan]: MonadPlan[F] = implicitly
}

case class PlanConfig(warpSpeed: WarpSpeed)
case class PlanState(expedition: Expedition, dilithium: Dilithium)

class ExpeditionPlanning[P[_]](implicit M: Monad[P], P: MonadPlan[P]) {
    import M.pure, M.ifM, P.ask.ask, P.get, P.modify

    type Plan = P[Free[FreeApplicative[Systems.Op, ?], Unit]]
    type PlanList = P[List[Free[FreeApplicative[Systems.Op, ?], Unit]]]

    def markExpendedDilithium(expended: Dilithium): P[Unit] =
        modify(s => s.copy(dilithium = s.dilithium - expended))

    def popFirstEncounter(): P[Encounter] = get.map(_.expedition.head) >>= { e =>
        modify(s => s.copy(expedition = s.expedition.tail)).map(_ => e)
    }

    def startIgnition(implicit systems: Systems[Systems.Op]): Plan =
        pure(systems.warpCore.turnCrankShaft)

    def planForEmptySpace(implicit systems: Systems[Systems.Op]): Plan =
        pure(systems.warpDrive.reattenuatePlasmaConduit)

    def planForNebula(implicit systems: Systems[Systems.Op]): Plan =
        pure(systems.warpDrive.disentangleElectroPlasmaSystem)

    def planForSpaceCrab(implicit systems: Systems[Systems.Op]): Plan = pure {
        import systems._
        for {
            _ <- deflectorShields.repolarizeFrequencyModulator(DeflectorPolarity.Parallel)
            _ <- phasers.focusOptronics(PhaserFrequency.NinetyNineTHz)
            _ <- phasers.purgePrefireChamber
        } yield ()
    }

    def planForAsteroidField(implicit systems: Systems[Systems.Op]): Plan = pure {
        import systems._
        for {
            _ <- warpCore.engageAntimatterReactionAssembly
            _ <- deflectorShields.repolarizeFrequencyModulator(DeflectorPolarity.Antiparallel)
        } yield ()
    }

    def planForSpaceTimeAnomaly(implicit systems: Systems[Systems.Op]): Plan = pure {
        import systems._
        for {
            _ <- phasers.rewirePowerCellMatrix
            _ <- deflectorShields.decompressGravitonEmitter
        } yield ()
    }

    def planEncounter: Encounter => Plan = {
        case EmptySpace => planForEmptySpace
        case Nebula => planForNebula
        case HostileSpaceCrab => planForSpaceCrab
        case AsteroidField => planForAsteroidField
        case SpaceTimeAnomaly => planForSpaceTimeAnomaly
    }

    def planWarpJump(implicit systems: Systems[Systems.Op]): Plan = {
        import systems._
        for {
            warpSpeed <- ask map (_.warpSpeed)
            dilithium <- get map (_.dilithium)
            w = math.min(warpSpeed, dilithium)
            _ <- markExpendedDilithium(w)
        }
        yield for {
            _ <- warpCore.convertDilithiumCrystals(w)
            _ <- warpDrive.tuneWarpFieldCoil(w)
        } yield ()
    }

    def planWarpToEncounter(e: Encounter): Plan = {
        for {
            warpJump <- planWarpJump
            encounter <- planEncounter(e)
        }
        yield warpJump >> encounter
    }

    def planExpeditionStages(): PlanList = {
        ifM(get map (_.expedition.isEmpty))(
            pure(List.empty),
            for {
                e <- popFirstEncounter
                h <- planWarpToEncounter(e)
                t <- planExpeditionStages
            }
            yield h +: t
        )
    }

    def planExpedition(): PlanList = {
        for {
            ignition <- startIgnition
            stages <- planExpeditionStages
        }
        yield ignition +: stages
    }
}
