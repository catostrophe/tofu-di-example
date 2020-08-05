package tofu.examples.di.algebras

import derevo.derive
import tofu.data.derived.ContextEmbed
import tofu.examples.di.util.Summoner
import tofu.higherKind.derived.representableK

@derive(representableK)
trait HttpClient[F[_]] {
  def send(request: String): F[String]
}

object HttpClient extends Summoner[HttpClient] with ContextEmbed[HttpClient]
