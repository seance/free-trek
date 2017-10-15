package freetrek.cats.execution

import freetrek.cats.planning._
import cats._
import cats.data._

trait Execution {

    type P[A] = ReaderT[State[PlanState, ?], PlanConfig, A]

    implicit val P: MonadPlan[P] = new MonadPlan[P] {
        // Members declared in cats.Applicative
        def pure[A](x: A): P[A] = Kleisli(_ => State.pure(x))

        // Members declared in cats.FlatMap
        def flatMap[A, B](fa: P[A])(f: A => P[B]): P[B] = fa.flatMap(f)
        def tailRecM[A, B](a: A)(f: A => P[Either[A, B]]): P[B] =
            f(a).flatMap(_.fold(tailRecM(_)(f), pure)) // ?

        // Members declared in cats.MonadReader
        def ask: P[PlanConfig] = ReaderT(e => State.pure(e))
        def local[A](f: PlanConfig => PlanConfig)(fa: P[A]): P[A] =
            Kleisli(f andThen fa.run)

        // Members declared in cats.MonadState
        def get: P[PlanState] = ReaderT(_ => State.get)
        def set(s: PlanState): P[Unit] = ReaderT(_ => State.set(s))
    }

    def planner = new ExpeditionPlanning[P]
}
