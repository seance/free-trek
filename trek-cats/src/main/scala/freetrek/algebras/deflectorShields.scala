package freetrek.cats.algebras

import freetrek.domain._
import DeflectorPolarity._
import cats.free._

sealed trait DeflectorShieldsOps[A] extends SystemsOps
case class DecompressGravitonEmitter() extends DeflectorShieldsOps[Unit]
case class RepolarizeFrequencyModulator(polarity: DeflectorPolarity) extends DeflectorShieldsOps[Unit]

class DeflectorShields[F[_]](implicit i: Inject[DeflectorShieldsOps, F]) {
    def decompressGravitonEmitter = Free.inject(DecompressGravitonEmitter())
    def repolarizeFrequencyModulator(polarity: DeflectorPolarity) = Free.inject(RepolarizeFrequencyModulator(polarity))
}

object DeflectorShields {
    implicit def apply[F[_]](implicit i: Inject[DeflectorShieldsOps, F]) = new DeflectorShields
}
