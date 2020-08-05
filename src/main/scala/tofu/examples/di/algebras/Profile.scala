package tofu.examples.di.algebras

import derevo.derive
import tofu.data.derived.ContextEmbed
import tofu.examples.di.util.Summoner
import tofu.higherKind.derived.representableK

@derive(representableK)
trait Profile[F[_]] {
  def profile(what: String): F[Unit]
}

object Profile extends Summoner[Profile] with ContextEmbed[Profile]
