package freetrek.frees.interpreters

import freetrek.domain.{WarpSpeed, Dilithium, PhaserFrequency, DeflectorPolarity}
import PhaserFrequency._
import DeflectorPolarity._
import freetrek.frees.algebras._
import cats.data._
import cats.implicits._

trait Interpreters {

    /**
     * No super type available. Systems.Op[Unit] using CopK.Inject would make
     * test errors noisy.
     */
    type SystemsOps = Any

    type Trace[A] = Writer[List[SystemsOps], A]

    import WarpCore._
    import WarpDrive._
    import Phasers._
    import DeflectorShields._

    implicit val warpCoreTraceInterpreter = new WarpCore.Handler[Trace] {
        def turnCrankShaft() = ().writer(List(TurnCrankShaftOp()))
        def engageAntimatterReactionAssembly() = ().writer(List(EngageAntimatterReactionAssemblyOp()))
        def convertDilithiumCrystals(crystals: Dilithium) = ().writer(List(ConvertDilithiumCrystalsOp(crystals)))
    }

    implicit val warpDriveTraceInterpreter = new WarpDrive.Handler[Trace] {
        def reattenuatePlasmaConduit() = ().writer(List(ReattenuatePlasmaConduitOp()))
        def tuneWarpFieldCoil(speed: WarpSpeed) = ().writer(List(TuneWarpFieldCoilOp(speed)))
        def disentangleElectroPlasmaSystem() = ().writer(List(DisentangleElectroPlasmaSystemOp()))
    }

    implicit val phasersTraceInterpreter = new Phasers.Handler[Trace] {
        def focusOptronics(frequency: PhaserFrequency) = ().writer(List(FocusOptronicsOp(frequency)))
        def rewirePowerCellMatrix() = ().writer(List(RewirePowerCellMatrixOp()))
        def purgePrefireChamber() = ().writer(List(PurgePrefireChamberOp()))
    }

    implicit val deflectorShieldsTraceInterpreter = new DeflectorShields.Handler[Trace] {
        def decompressGravitonEmitter() = ().writer(List(DecompressGravitonEmitterOp()))
        def repolarizeFrequencyModulator(polarity: DeflectorPolarity) = ().writer(List(RepolarizeFrequencyModulatorOp(polarity)))
    }
}
