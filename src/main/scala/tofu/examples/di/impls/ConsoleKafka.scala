package tofu.examples.di.impls

import cats.effect.Resource
import cats.instances.string._
import cats.{Monad, Show}
import tofu.HasContext
import tofu.common.Console
import tofu.examples.di.config.Config.KafkaConfig
import tofu.examples.di.algebras.{Kafka, Wrapper}
import tofu.syntax.console._
import tofu.syntax.context._
import tofu.syntax.monadic._

class ConsoleKafka[F[_]: Console: *[_] HasContext KafkaConfig: Monad: Wrapper] extends Kafka[F] {
  def send[A: Show](topic: String, body: A): F[Unit] =
    context[F] >>= (cfg => Wrapper[F].wrap(puts"Sending '$body' to topic '$topic' of '${cfg.url}'")("kafka msg"))
}

object ConsoleKafka {
  def make[I[_]: Monad, F[_]: Console: *[_] HasContext KafkaConfig: Monad: Wrapper]: Resource[I, Kafka[F]] =
    new ConsoleKafka[F].pure[Resource[I, *]]
}
