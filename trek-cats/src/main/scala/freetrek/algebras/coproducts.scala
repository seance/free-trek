package freetrek.cats.algebras

import cats.data._

trait SystemsOps

trait Coproducts {

    type C0[A] = Coproduct[WarpCoreOps, WarpDriveOps, A]
    type C1[A] = Coproduct[PhasersOps, C0, A]
    type C2[A] = Coproduct[DeflectorShieldsOps, C1, A]

    type Systems[A] = C2[A]
}
