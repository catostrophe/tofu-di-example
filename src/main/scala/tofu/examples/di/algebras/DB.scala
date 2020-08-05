package tofu.examples.di.algebras

import derevo.derive
import tofu.data.derived.ContextEmbed
import tofu.examples.di.util.Summoner
import tofu.higherKind.derived.representableK

@derive(representableK)
trait DB[F[_]] {
  def get(key: Int): F[String]
}

object DB extends Summoner[DB] with ContextEmbed[DB]
