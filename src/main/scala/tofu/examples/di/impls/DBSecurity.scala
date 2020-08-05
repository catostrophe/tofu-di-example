package tofu.examples.di.impls

import cats.Monad
import cats.effect.Resource
import tofu.common.Console
import tofu.examples.di.algebras.{DB, Profile, Security}
import tofu.examples.di.model.{Login, Operation, User}
import tofu.syntax.console._
import tofu.syntax.monadic._

class DBSecurity[F[_]: Monad: Console: DB: Profile] extends Security[F] {
  def authenticate(login: Login): F[User] =
    for {
      r <- DB[F].get(login.length)
    } yield User(s"From '$login' got '$r'")

  def checkAuth(user: User, op: Operation): F[Boolean] =
    for {
      _ <- Profile[F].profile(user.name)
      _ <- puts"Authing user '$user'"
    } yield op.startsWith("i am okay")
}

object DBSecurity {
  def make[I[_]: Monad, F[_]: Monad: Console: DB: Profile]: Resource[I, Security[F]] =
    new DBSecurity[F].pure[Resource[I, *]]
}
