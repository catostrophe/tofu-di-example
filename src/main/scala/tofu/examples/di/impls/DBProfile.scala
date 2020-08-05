package tofu.examples.di.impls

import cats.Monad
import cats.effect.Resource
import cats.instances.string._
import tofu.common.Console
import tofu.examples.di.algebras.{DB, Profile, Security}
import tofu.syntax.console._
import tofu.syntax.monadic._

class DBProfile[F[_]: Monad: Console: DB: Security] extends Profile[F] {
  def profile(what: String): F[Unit] = puts"Profiling: $what"
}

object DBProfile {
  def make[I[_]: Monad, F[_]: Monad: Console: DB: Security]: Resource[I, Profile[F]] =
    new DBProfile[F].pure[Resource[I, *]]
}
