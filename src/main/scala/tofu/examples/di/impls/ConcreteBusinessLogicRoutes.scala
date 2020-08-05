package tofu.examples.di.impls

import cats.Monad
import cats.effect.Resource
import cats.syntax.option._
import tofu.examples.di.algebras.{BusinessLogic, BusinessLogicRoutes}
import tofu.examples.di.model.{Req, Resp}
import tofu.syntax.monadic._

class ConcreteBusinessLogicRoutes[F[_]: Monad: BusinessLogic] extends BusinessLogicRoutes[F] {
  def routes: Req[F] => F[Option[Resp[F]]] = {
    case Req(header, body) if header == "businessRequest" =>
      body.flatMap {
        _.toIntOption.fold(Resp("businessResponse", "400: BAD REQUEST".pure[F]).some.pure[F]) {
          id =>
            BusinessLogic[F].process(id).as(Resp("businessResponse", "200: OK".pure[F]).some)
        }
      }
    case _ => none[Resp[F]].pure[F]
  }
}
object ConcreteBusinessLogicRoutes {
  def make[I[_]: Monad, F[_]: Monad: BusinessLogic]: Resource[I, BusinessLogicRoutes[F]] =
    new ConcreteBusinessLogicRoutes[F].pure[Resource[I, *]]
}
