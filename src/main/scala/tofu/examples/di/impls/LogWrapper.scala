package tofu.examples.di.impls

import cats.Monad
import cats.instances.string._
import tofu.common.Console
import tofu.examples.di.algebras.Wrapper
import tofu.syntax.console._
import tofu.syntax.monadic._

class LogWrapper[F[_]: Monad: Console] extends Wrapper[F] {
  def wrap[A](fa: F[A])(op: String): F[A] = puts"Starting operation $op" >> fa <* puts"Finished operation $op"
}

object LogWrapper {
  def make[F[_]: Monad: Console]: Wrapper[F] = new LogWrapper[F]
}
