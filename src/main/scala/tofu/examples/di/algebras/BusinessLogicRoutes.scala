package tofu.examples.di.algebras

import cats.{FlatMap, Functor, ~>}
import cats.tagless.syntax.functorK._
import tofu.data.derived.ContextEmbed
import tofu.examples.di.model.{Req, Resp}
import tofu.examples.di.util.Summoner
import tofu.higherKind.Embed
import tofu.lift.IsoK
import tofu.syntax.monadic._

trait BusinessLogicRoutes[F[_]] {
  def routes: Req[F] => F[Option[Resp[F]]]
}

object BusinessLogicRoutes extends Summoner[BusinessLogicRoutes] with ContextEmbed[BusinessLogicRoutes] {
  implicit val bizRoutesEbmed: Embed[BusinessLogicRoutes] = new Embed[BusinessLogicRoutes] {
    def embed[F[_]](ft: F[BusinessLogicRoutes[F]])(implicit F: FlatMap[F]): BusinessLogicRoutes[F] =
      new BusinessLogicRoutes[F] {
        def routes: Req[F] => F[Option[Resp[F]]] = req => F.flatMap(ft)(_.routes(req))
      }
  }

  implicit final class Ops[F[_]](private val self: BusinessLogicRoutes[F]) extends AnyVal {
    def imapK[G[_]: Functor](fk: F ~> G)(gk: G ~> F): BusinessLogicRoutes[G] =
      new BusinessLogicRoutes[G] {
        def routes: Req[G] => G[Option[Resp[G]]] = reqG => fk(self.routes(reqG.mapK(gk))).map(_.map(_.mapK(fk)))
      }
    def ilift[G[_]: Functor](implicit isoK: IsoK[F, G]): BusinessLogicRoutes[G] = imapK(isoK.tof)(isoK.fromF)
  }
}
