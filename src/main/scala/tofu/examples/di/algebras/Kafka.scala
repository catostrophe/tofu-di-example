package tofu.examples.di.algebras

import cats.Show
import derevo.derive
import tofu.data.derived.ContextEmbed
import tofu.examples.di.util.Summoner
import tofu.higherKind.derived.representableK

@derive(representableK)
trait Kafka[F[_]] {
  def send[A: Show](topic: String, body: A): F[Unit]
}

object Kafka extends Summoner[Kafka] with ContextEmbed[Kafka]
