package freetrek.frees

import freetrek.domain.{WarpSpeed, Dilithium, PhaserFrequency, DeflectorPolarity}
import PhaserFrequency._
import DeflectorPolarity._
import freestyle._
import freestyle.implicits._

package object algebras {

    @free
    trait WarpCore {
        def turnCrankShaft(): FS[Unit]
        def engageAntimatterReactionAssembly(): FS[Unit]
        def convertDilithiumCrystals(crystals: Dilithium): FS[Unit]
    }

    @free
    trait WarpDrive {
        def reattenuatePlasmaConduit: FS[Unit]
        def tuneWarpFieldCoil(speed: WarpSpeed): FS[Unit]
        def disentangleElectroPlasmaSystem: FS[Unit]
    }

    @free
    trait Phasers {
        def focusOptronics(frequency: PhaserFrequency): FS[Unit]
        def rewirePowerCellMatrix: FS[Unit]
        def purgePrefireChamber: FS[Unit]
    }

    @free
    trait DeflectorShields {
        def decompressGravitonEmitter: FS[Unit]
        def repolarizeFrequencyModulator(polarity: DeflectorPolarity): FS[Unit]
    }

    @module
    trait Systems {
        val warpCore: WarpCore
        val warpDrive: WarpDrive
        val phasers: Phasers
        val deflectorShields: DeflectorShields
    }
}
