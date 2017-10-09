package freetrek.cats.interpreters

import freetrek.cats.algebras._
import cats._
import cats.data._
import cats.implicits._

trait Interpreters extends Coproducts {

    type Trace[A] = Writer[List[SystemsOps], A]

    val warpCoreTraceInterpreter = new (WarpCoreOps ~> Trace) {
        def apply[A](fa: WarpCoreOps[A]): Trace[A] = fa match {
            case TurnCrankShaft() => ().writer(List(fa))
            case EngageAntimatterReactionAssembly() => ().writer(List(fa))
            case ConvertDilithiumCrystals(crystals) => ().writer(List(fa))
        }
    }

    val warpDriveTraceInterpreter = new (WarpDriveOps ~> Trace) {
        def apply[A](fa: WarpDriveOps[A]): Trace[A] = fa match {
            case ReattenuatePlasmaConduit() => ().writer(List(fa))
            case TuneWarpFieldCoil(speed) => ().writer(List(fa))
            case DisentangleElectroPlasmaSystem() => ().writer(List(fa))
        }
    }

    val phasersTraceInterpreter = new (PhasersOps ~> Trace) {
        def apply[A](fa: PhasersOps[A]): Trace[A] = fa match {
            case FocusOptronics(frequency) => ().writer(List(fa))
            case RewirePowerCellMatrix() => ().writer(List(fa))
            case PurgePrefireChamber() => ().writer(List(fa))
        }
    }

    val deflectorShieldsTraceInterpreter = new (DeflectorShieldsOps ~> Trace) {
        def apply[A](fa: DeflectorShieldsOps[A]): Trace[A] = fa match {
            case DecompressGravitonEmitter() => ().writer(List(fa))
            case RepolarizeFrequencyModulator(polarity) => ().writer(List(fa))
        }
    }

    val systemsTraceInterpreter: Systems ~> Trace =
        deflectorShieldsTraceInterpreter or (
            phasersTraceInterpreter or (
                warpCoreTraceInterpreter or (
                    warpDriveTraceInterpreter
                )
            )
        )
}
