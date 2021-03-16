package tofu.examples.di.env

import cats.effect.Timer
import cats.tagless.syntax.functorK._
import cats.tagless.syntax.invariantK._
import cats.{Applicative, Defer, Functor, ~>}
import io.janstenpickle.trace4cats.Span
import io.janstenpickle.trace4cats.inject.EntryPoint
import tofu.examples.di.algebras._
import tofu.examples.di.config.Config
import tofu.examples.di.util.syntax.trace._
import tofu.generate.GenRandom
import tofu.optics.macros._

import scala.annotation.unused

@ClassyOptics
case class BaseEnv[F[_]](
  @promote config: Config,
  httpClient: HttpClient[F],
  database: DB[F],
  kafka: Kafka[F],
  security: Security[F],
  profile: Profile[F],
  entryPoint: EntryPoint[F],
  timer: Timer[F],
  genRandom: GenRandom[F],
  wrapper: Wrapper[F]
) {
  final def imapK[G[_]: Defer: Applicative](f: F ~> G)(g: G ~> F): BaseEnv[G] =
    BaseEnv(
      config,
      httpClient.mapK(f),
      database.mapK(f),
      kafka.mapK(f),
      security.mapK(f),
      profile.mapK(f),
      entryPoint.mapK(f),
      timer.mapK(f),
      genRandom.mapK(f),
      wrapper.imapK(f)(g)
    )
}

@ClassyOptics
case class BizEnv[F[_]](bizLogic: BusinessLogic[F], bizRoutes: BusinessLogicRoutes[F]) {
  final def imapK[G[_]: Functor](f: F ~> G)(g: G ~> F): BizEnv[G] =
    BizEnv(bizLogic.mapK(f), bizRoutes.imapK(f)(g))
}

@ClassyOptics
case class Ctx[F[_]](span: Span[F]) {
  final def imapK[G[_]: Defer: Applicative](f: F ~> G)(@unused g: G ~> F): Ctx[G] =
    Ctx(span.mapK(f))
}

@ClassyOptics
case class CtxEnv[F[_]](@promote base: BaseEnv[F], @promote biz: BizEnv[F], @promote ctx: Ctx[F]) {
  final def imapK[G[_]: Defer: Applicative](f: F ~> G)(g: G ~> F): CtxEnv[G] =
    CtxEnv(base.imapK(f)(g), biz.imapK(f)(g), ctx.imapK(f)(g))
}
