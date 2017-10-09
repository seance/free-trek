package freetrek.cats.algebras

import freetrek.domain._
import cats.free._

sealed trait WarpDriveOps[A] extends SystemsOps
case class ReattenuatePlasmaConduit() extends WarpDriveOps[Unit]
case class TuneWarpFieldCoil(speed: WarpSpeed) extends WarpDriveOps[Unit]
case class DisentangleElectroPlasmaSystem() extends WarpDriveOps[Unit]

class WarpDrive[F[_]](implicit i: Inject[WarpDriveOps, F]) {
    def reattenuatePlasmaConduit = Free.inject(ReattenuatePlasmaConduit())
    def tuneWarpFieldCoil(speed: WarpSpeed) = Free.inject(TuneWarpFieldCoil(speed))
    def disentangleElectroPlasmaSystem = Free.inject(DisentangleElectroPlasmaSystem())
}

object WarpDrive {
    implicit def apply[F[_]](implicit i: Inject[WarpDriveOps, F]) = new WarpDrive
}
