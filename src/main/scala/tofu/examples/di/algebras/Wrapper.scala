package tofu.examples.di.algebras

import derevo.derive
import derevo.tagless.invariantK
import tofu.data.derived.ContextEmbed
import tofu.examples.di.util.Summoner
import tofu.higherKind.derived.embed

@derive(invariantK, embed)
trait Wrapper[F[_]] {
  def wrap[A](fa: F[A])(op: String): F[A]
}
object Wrapper extends Summoner[Wrapper] with ContextEmbed[Wrapper]
