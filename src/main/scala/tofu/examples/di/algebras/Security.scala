package tofu.examples.di.algebras

import derevo.derive
import tofu.data.derived.ContextEmbed
import tofu.examples.di.model.{Login, Operation, User}
import tofu.examples.di.util.Summoner
import tofu.higherKind.derived.representableK

@derive(representableK)
trait Security[F[_]] {
  def authenticate(login: Login): F[User]
  def checkAuth(user: User, op: Operation): F[Boolean]
}

object Security extends Summoner[Security] with ContextEmbed[Security]
