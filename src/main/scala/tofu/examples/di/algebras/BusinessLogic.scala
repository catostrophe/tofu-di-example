package tofu.examples.di.algebras

import derevo.derive
import tofu.data.derived.ContextEmbed
import tofu.examples.di.util.Summoner
import tofu.higherKind.derived.representableK

@derive(representableK)
trait BusinessLogic[F[_]] {
  def process(id: Int): F[Unit]
}

object BusinessLogic extends Summoner[BusinessLogic] with ContextEmbed[BusinessLogic]
