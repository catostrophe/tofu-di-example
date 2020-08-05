package tofu.examples.di.algebras

import derevo.derive
import tofu.higherKind.derived.representableK

@derive(representableK)
trait Server[F[_]] {
  def serve: F[Unit]
}
