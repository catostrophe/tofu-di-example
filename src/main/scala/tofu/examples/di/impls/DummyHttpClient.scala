package tofu.examples.di.impls

import cats.Monad
import cats.effect.Resource
import tofu.HasContext
import tofu.examples.di.algebras.HttpClient
import tofu.examples.di.config.Config.HttpConfig
import tofu.syntax.context._
import tofu.syntax.monadic._

class DummyHttpClient[F[_]: Monad: *[_] HasContext HttpConfig] extends HttpClient[F] {
  def send(request: String): F[String] =
    for {
      cfg <- context[F]
    } yield s"Sent away request $request to ${cfg.url}"
}

object DummyHttpClient {
  def make[I[_]: Monad, F[_]: Monad: *[_] HasContext HttpConfig]: Resource[I, HttpClient[F]] =
    new DummyHttpClient[F].pure[Resource[I, *]]
}
