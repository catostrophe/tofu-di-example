package tofu.examples.di.impls

import cats.effect.{Resource, Timer}
import cats.instances.string._
import cats.Monad
import io.janstenpickle.trace4cats.inject.EntryPoint
import tofu.common.Console
import tofu.generate.GenRandom
import tofu.lift.IsoK
import tofu.BracketThrow
import tofu.examples.di.algebras.{BusinessLogicRoutes, Server}
import tofu.examples.di.env.Ctx
import tofu.examples.di.model.Req
import tofu.syntax.console._
import tofu.syntax.monadic._

import scala.concurrent.duration._

class ConcreteServer[F[_]: BracketThrow: Timer: EntryPoint: GenRandom: Console, G[_]: BusinessLogicRoutes](
  mkIsoK: Ctx[F] => IsoK[F, G]
) extends Server[F] {
  def serve: F[Unit] = (Timer[F].sleep(1.second) >> loop).foreverM

  def loop: F[Unit] =
    implicitly[EntryPoint[F]].root("serve-event").use {
      span =>
        implicit val isoK: IsoK[G, F] = mkIsoK(Ctx(span)).inverse
        val brF: BusinessLogicRoutes[F] = BusinessLogicRoutes[G].ilift[F]
        for {
          rnd <- GenRandom.nextInt(1000)
          req =
            if (rnd < 700) Req("businessRequest", (if (rnd < 400) s"$rnd" else "xxx").pure[F])
            else Req("badRequest", s"$rnd".pure[F])
          bdy <- req.body
          _ <- puts"Accepted request: header = ${req.header}, body = $bdy"
          resp <- brF.routes(req)
          _ <- resp.fold(puts"Server response: 404 NOT FOUND") {
            r =>
              r.body >>= (bdy => puts"Sent response: header = ${r.header}, body = $bdy")
          }
          _ <- puts"-------------------------------------------------------------"
        } yield ()
    }
}

object ConcreteServer {
  def make[I[_]: Monad, F[_]: BracketThrow: Timer: EntryPoint: GenRandom: Console, G[_]: BusinessLogicRoutes](
    mkIsoK: Ctx[F] => IsoK[F, G]
  ): Resource[I, Server[F]] =
    new ConcreteServer[F, G](mkIsoK).pure[Resource[I, *]]
}
