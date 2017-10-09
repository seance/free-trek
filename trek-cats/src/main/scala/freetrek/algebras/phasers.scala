package freetrek.cats.algebras

import freetrek.domain._
import PhaserFrequency._
import cats.free._

sealed trait PhasersOps[A] extends SystemsOps
case class FocusOptronics(frequency: PhaserFrequency) extends PhasersOps[Unit]
case class RewirePowerCellMatrix() extends PhasersOps[Unit]
case class PurgePrefireChamber() extends PhasersOps[Unit]

class Phasers[F[_]](implicit i: Inject[PhasersOps, F]) {
    def focusOptronics(frequency: PhaserFrequency) = Free.inject(FocusOptronics(frequency))
    def rewirePowerCellMatrix = Free.inject(RewirePowerCellMatrix())
    def purgePrefireChamber = Free.inject(PurgePrefireChamber())
}

object Phasers {
    implicit def apply[F[_]](implicit i: Inject[PhasersOps, F]) = new Phasers
}
