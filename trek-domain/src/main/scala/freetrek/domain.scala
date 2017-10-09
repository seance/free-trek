package freetrek.domain

sealed trait Encounter
case object EmptySpace extends Encounter
case object Nebula extends Encounter
case object AsteroidField extends Encounter
case object HostileSpaceCrab extends Encounter
case object SpaceTimeAnomaly extends Encounter

object `package` {
    type Expedition = List[Encounter]
    type Dilithium = Int
    type WarpSpeed = Int
}

object PhaserFrequency extends Enumeration {
    type PhaserFrequency = Value
    val NinetyNineTHz = Value("99 THz")
    val TwoKHz = Value("2 kHz")
}

object DeflectorPolarity extends Enumeration {
    type DeflectorPolarity = Value
    val Parallel = Value("Parallel")
    val Antiparallel = Value("Antiparallel")
}

import PhaserFrequency.PhaserFrequency
import DeflectorPolarity.DeflectorPolarity

trait WarpCore {
    def turnCrankShaft(): Unit
    def engageAntimatterReactionAssembly(): Unit
    def convertDilithiumCrystals(crystals: Dilithium): Unit
}

trait WarpDrive {
    def reattenuatePlasmaConduit: Unit
    def tuneWarpFieldCoil(speed: WarpSpeed): Unit
    def disentangleElectroPlasmaSystem: Unit
}

trait Phasers {
    def focusOptronics(frequency: PhaserFrequency): Unit
    def rewirePowerCellMatrix: Unit
    def purgePrefireChamber: Unit
}

trait DeflectorShields {
    def decompressGravitonEmitter: Unit
    def repolarizeFrequencyModulator(polarity: DeflectorPolarity): Unit
}
