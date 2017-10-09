package freetrek.cats.algebras

import freetrek.domain._
import cats.free._

sealed trait WarpCoreOps[A] extends SystemsOps
case class TurnCrankShaft() extends WarpCoreOps[Unit]
case class EngageAntimatterReactionAssembly() extends WarpCoreOps[Unit]
case class ConvertDilithiumCrystals(crystals: Dilithium) extends WarpCoreOps[Unit]

class WarpCore[F[_]](implicit i: Inject[WarpCoreOps, F]) {
    def turnCrankShaft() = Free.inject(TurnCrankShaft())
    def engageAntimatterReactionAssembly() = Free.inject(EngageAntimatterReactionAssembly())
    def convertDilithiumCrystals(crystals: Dilithium) = Free.inject(ConvertDilithiumCrystals(crystals))
}

object WarpCore {
    implicit def apply[F[_]](implicit i: Inject[WarpCoreOps, F]) = new WarpCore
}
