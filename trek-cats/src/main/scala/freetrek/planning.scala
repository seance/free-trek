package freetrek.cats.planning

import freetrek.domain.{Expedition, Encounter, Dilithium, DeflectorPolarity, PhaserFrequency}
import freetrek.domain.{EmptySpace, Nebula, HostileSpaceCrab, AsteroidField, SpaceTimeAnomaly}
import DeflectorPolarity._
import PhaserFrequency._
import freetrek.cats.algebras._
import cats._
import cats.implicits._
import cats.free._

trait MonadPlan[F[_]]
    extends MonadReader[F, PlanConfig]
    with MonadState[F, PlanState]

object MonadPlan {
    def apply[F[_]: MonadPlan]: MonadPlan[F] = implicitly
}

case class PlanConfig(warpSpeed: Int)
case class PlanState(expedition: Expedition, dilithium: Int)

class ExpeditionPlanning[P[_]](implicit M: MonadPlan[P]) extends Coproducts {
    import M._

    type Plan = P[Free[Systems, Unit]]
    type PlanList = P[List[Free[Systems, Unit]]]

    def markExpendedDilithium(expended: Dilithium): P[Unit] =
        modify(s => s.copy(dilithium = s.dilithium - expended))

    def popFirstEncounter(): P[Encounter] = get.map(_.expedition.head) >>= { e =>
        modify(s => s.copy(expedition = s.expedition.tail)).map(_ => e)
    }

    def startIgnition(implicit C: WarpCore[Systems]): Plan =
        pure(C.turnCrankShaft())

    def planForEmptySpace(implicit D: WarpDrive[Systems]): Plan =
        pure(D.reattenuatePlasmaConduit)

    def planForNebula(implicit D: WarpDrive[Systems]): Plan =
        pure(D.disentangleElectroPlasmaSystem)

    def planForSpaceCrab(implicit P: Phasers[Systems], S: DeflectorShields[Systems]): Plan = pure {
        S.repolarizeFrequencyModulator(DeflectorPolarity.Parallel) >>
        P.focusOptronics(PhaserFrequency.NinetyNineTHz) >>
        P.purgePrefireChamber
    }

    def planForAsteroidField(implicit C: WarpCore[Systems], S: DeflectorShields[Systems]): Plan = pure {
        C.engageAntimatterReactionAssembly >>
        S.repolarizeFrequencyModulator(DeflectorPolarity.Antiparallel)
    }

    def planForSpaceTimeAnomaly(implicit P: Phasers[Systems], S: DeflectorShields[Systems]): Plan = pure {
        P.rewirePowerCellMatrix >>
        S.decompressGravitonEmitter
    }

    def planEncounter: Encounter => Plan = {
        case EmptySpace => planForEmptySpace
        case Nebula => planForNebula
        case HostileSpaceCrab => planForSpaceCrab
        case AsteroidField => planForAsteroidField
        case SpaceTimeAnomaly => planForSpaceTimeAnomaly
    }

    def planWarpJump(implicit C: WarpCore[Systems], D: WarpDrive[Systems]): Plan = {
        for {
            warpSpeed <- ask map (_.warpSpeed)
            dilithium <- get map (_.dilithium)
            w = math.min(warpSpeed, dilithium)
            _ <- markExpendedDilithium(w)
        }
        yield {
            C.convertDilithiumCrystals(w) >>
            D.tuneWarpFieldCoil(w)
        }
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
