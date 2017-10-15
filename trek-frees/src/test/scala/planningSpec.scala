package freetrek.frees.test

import freetrek.domain._
import freetrek.frees.execution.{Execution}
import freetrek.frees.interpreters.{Interpreters}
import freetrek.frees.planning.{PlanConfig, PlanState}
import freetrek.frees.algebras._
import org.scalatest.{FlatSpec, Matchers}
import freestyle._
import freestyle.implicits._
import cats.implicits._

class ExpeditionPlanningSpec extends FlatSpec with Matchers with ExpeditionChecks {

    behavior of "Freestyle based expedition planning"

    it should "at least start up the Enterprise" in {
        checkExpeditionPlan(0, 10, Nil) { (dilithium, stages) =>
            dilithium should === (10)
            stages should equal (
                startIgnition :: Nil
            )
        }
    }

    it should "track dilithium usage in a single encounter" in {
        checkExpeditionPlan(5, 10, EmptySpace :: Nil) { (dilithium, stages) =>
            dilithium should === (5)
            stages should equal (
                startIgnition ::
                emptySpace(5) ::
                Nil
            )
        }
    }

    it should "track dilithium usage across multiple encounters" in {
        checkExpeditionPlan(3, 10, EmptySpace :: EmptySpace :: Nil) { (dilithium, stages) =>
            dilithium should === (4)
            stages should equal (
                startIgnition ::
                emptySpace(3) ::
                emptySpace(3) ::
                Nil
            )
        }
    }

    it should "lower warp speed when low on dilithium" in {
        checkExpeditionPlan(6, 10, EmptySpace :: EmptySpace :: Nil) { (dilithium, stages) =>
            dilithium should === (0)
            stages should equal (
                startIgnition ::
                emptySpace(6) ::
                emptySpace(4) ::
                Nil
            )
        }
    }

    it should "take the chance to reattenuate the plasma conduit in empty space" in {
        checkExpeditionPlan(6, 10, EmptySpace :: Nil) { (dilithium, stages) =>
            dilithium should === (4)
            stages should equal (
                startIgnition ::
                emptySpace(6) ::
                Nil
            )
        }
    }

    it should "disentangle the electro-plasma system in nebulas" in {
        checkExpeditionPlan(6, 10, Nebula :: Nil) { (dilithium, stages) =>
            dilithium should === (4)
            stages should equal (
                startIgnition ::
                nebula(6) ::
                Nil
            )
        }
    }

    it should "setup deflector polarity, phaser frequency and focus optronics when encountering space crabs" in {
        checkExpeditionPlan(6, 10, HostileSpaceCrab :: Nil) { (dilithium, stages) =>
            dilithium should === (4)
            stages should equal (
                startIgnition ::
                spaceCrab(6) ::
                Nil
            )
        }
    }

    it should "engage the antimatter reaction assembly and repolarize shields in asteroid fields" in {
        checkExpeditionPlan(9, 10, AsteroidField :: Nil) { (dilithium, stages) =>
            dilithium should === (1)
            stages should equal (
                startIgnition ::
                asteroidField(9) ::
                Nil
            )
        }
    }

    it should "take precaution by rewiring phaser power cell matrix and decompress the deflector graviton emitter near space-time anomalies" in {
        checkExpeditionPlan(4, 10, SpaceTimeAnomaly :: Nil) { (dilithium, stages) =>
            dilithium should === (6)
            stages should equal (
                startIgnition ::
                spaceTimeAnomaly(4) ::
                Nil
            )
        }
    }

    it should "correctly plan for long and varied space expeditions" in {
        val expedition = EmptySpace :: Nebula :: HostileSpaceCrab :: AsteroidField :: SpaceTimeAnomaly :: Nil
        checkExpeditionPlan(7, 30, expedition) { (dilithium, stages) =>
            dilithium should === (0)
            stages should equal (
                startIgnition ::
                emptySpace(7) ::
                nebula(7) ::
                spaceCrab(7) ::
                asteroidField(7) ::
                spaceTimeAnomaly(2) ::
                Nil
            )
        }
    }
}

trait ExpeditionChecks extends Execution with Interpreters {

    def checkExpeditionPlan
        (warpSpeed: WarpSpeed, dilithium: Dilithium, expedition: Expedition)
        (checks: (Dilithium, List[List[SystemsOps]]) => Unit)
    {
        val config = PlanConfig(warpSpeed)
        val state0 = PlanState(expedition, dilithium)
        val (state1, frees) = planner.planExpedition.run(config).run(state0).value
        val stages = frees.map(_.interpret[Trace].written)

        checks(state1.dilithium, stages)
    }

    import WarpCore._
    import WarpDrive._
    import Phasers._
    import DeflectorShields._

    def startIgnition = TurnCrankShaftOp() :: Nil

    def emptySpace(warpSpeed: WarpSpeed) =
        ConvertDilithiumCrystalsOp(warpSpeed) ::
        TuneWarpFieldCoilOp(warpSpeed) ::
        ReattenuatePlasmaConduitOp() ::
        Nil

    def nebula(warpSpeed: WarpSpeed) =
        ConvertDilithiumCrystalsOp(warpSpeed) ::
        TuneWarpFieldCoilOp(warpSpeed) ::
        DisentangleElectroPlasmaSystemOp() ::
        Nil

    def spaceCrab(warpSpeed: WarpSpeed) =
        ConvertDilithiumCrystalsOp(warpSpeed) ::
        TuneWarpFieldCoilOp(warpSpeed) ::
        RepolarizeFrequencyModulatorOp(DeflectorPolarity.Parallel) ::
        FocusOptronicsOp(PhaserFrequency.NinetyNineTHz) ::
        PurgePrefireChamberOp() ::
        Nil

    def asteroidField(warpSpeed: WarpSpeed) =
        ConvertDilithiumCrystalsOp(warpSpeed) ::
        TuneWarpFieldCoilOp(warpSpeed) ::
        EngageAntimatterReactionAssemblyOp() ::
        RepolarizeFrequencyModulatorOp(DeflectorPolarity.Antiparallel) ::
        Nil

    def spaceTimeAnomaly(warpSpeed: WarpSpeed) =
        ConvertDilithiumCrystalsOp(warpSpeed) ::
        TuneWarpFieldCoilOp(warpSpeed) ::
        RewirePowerCellMatrixOp() ::
        DecompressGravitonEmitterOp() ::
        Nil
}
