package freetrek.frees.execution

import freetrek.frees.algebras._
import freetrek.frees.interpreters._
import freetrek.frees.planning._
import freestyle.{Interpreters => _, _}
import freestyle.implicits._
import cats._
import cats.data._
import cats.implicits._
import cats.mtl._
import cats.mtl.implicits._

trait Execution {

    type P[A] = ReaderT[State[PlanState, ?], PlanConfig, A]

    implicit val monadPlan: MonadPlan[P] = new MonadPlan[P] {
        // Members declared in ApplicativeLocal
        val ask: ApplicativeAsk[P, PlanConfig] = askReader
        def local[A](f: PlanConfig => PlanConfig)(fa: P[A]): P[A] = Kleisli(f andThen fa.run)
        def scope[A](e: PlanConfig)(fa: P[A]): P[A] = Kleisli(_ => fa.run(e))

        // Members declared in MonadState
        val monad: Monad[P] = Monad[P]
        def get: P[PlanState] = Kleisli(_ => State.get)
        def inspect[A](f: PlanState => A): P[A] = Kleisli(_ => State.get[PlanState].map(f))
        def modify(f: PlanState => PlanState): P[Unit] = Kleisli(_ => State.modify(f))
        def set(s: PlanState): P[Unit] = Kleisli(_ => State.set(s))
    }

    def planner = new ExpeditionPlanning[P]
}
