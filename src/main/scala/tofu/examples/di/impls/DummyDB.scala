package tofu.examples.di.impls

import cats.Monad
import cats.effect.Resource
import tofu.HasContext
import tofu.examples.di.algebras.DB
import tofu.examples.di.config.Config.DBConfig
import tofu.syntax.context._
import tofu.syntax.monadic._

class DummyDB[F[_]: Monad: *[_] HasContext DBConfig] extends DB[F] {
  def get(key: Int): F[String] =
    context[F].map(cfg => s"Getting something from database ${cfg.connectionAddress} by key $key")
}

object DummyDB {
  def make[I[_]: Monad, F[_]: Monad: *[_] HasContext DBConfig]: Resource[I, DB[F]] =
    new DummyDB[F].pure[Resource[I, *]]
}
