package tofu.examples.di

import cats.effect.{ExitCode, IO, IOApp, Resource, Timer}
import cats.tagless.syntax.functorK._
import tofu.WithProvide
import tofu.concurrent._
import tofu.examples.di.algebras._
import tofu.examples.di.config.Config
import tofu.examples.di.env.{BaseEnv, BizEnv, Ctx, CtxEnv}
import tofu.examples.di.impls._
import tofu.examples.di.util.implicits.entryPoint._
import tofu.examples.di.util.implicits.genRandom._
import tofu.examples.di.util.implicits.timer._
import tofu.examples.di.util.implicits.trace._
import tofu.generate.GenRandom
import tofu.syntax.funk.funK

object TofuDIExample extends IOApp {

  type I[+A] = IO[A]
  type Init[+A] = Resource[I, A]
  type Eff[+A] = ContextT[I, BaseEnv, A]
  type CtxEff[+A] = ContextT[Eff, CtxEnv, A]

  def run(args: List[String]): I[ExitCode] = {
    val res: Resource[I, Server[I]] = for {
      baseEnv <- initBaseEnv
      bizEnv <- initBizEnv
      implicit0(wp: WithProvide[CtxEff, Eff, Ctx[Eff]]) = mkProvide(baseEnv, bizEnv)
      server <- ConcreteServer.make[I, Eff, CtxEff]
    } yield server.mapK(funK(_.run(baseEnv)))

    res.use(_.serve).as(ExitCode.Success)
  }

  def initBaseEnv: Init[BaseEnv[Eff]] =
    for {
      config <- Resource.eval(Config.load[I])
      db <- DummyDB.make[I, Eff]
      security <- DBSecurity.make[I, Eff]
      profile <- DBProfile.make[I, Eff]
      kafka <- ConsoleKafka.make[I, Eff]
      httpClient <- DummyHttpClient.make[I, Eff]
      entryPoint <- JaegerEntryPoint.make[I, Eff]
      genRandom: GenRandom[Eff] <- Resource.eval(GenRandom.instance[I, Eff]())
      _timer: Timer[Eff] = Timer[I].mapK(ContextT.liftF)
      wrapper = LogWrapper.make[Eff]
    } yield BaseEnv(
      config = config,
      httpClient = httpClient,
      database = db,
      kafka = kafka,
      security = security,
      profile = profile,
      entryPoint = entryPoint,
      timer = _timer,
      genRandom = genRandom,
      wrapper = wrapper
    )

  def initBizEnv: Init[BizEnv[CtxEff]] =
    for {
      bizLogic <- ConcreteBusinessLogic.make[I, CtxEff]
      bizRoutes <- ConcreteBusinessLogicRoutes.make[I, CtxEff]
    } yield BizEnv(bizLogic = bizLogic, bizRoutes = bizRoutes)

  //TODO: could it be done easier?
  def mkProvide(baseEnv: BaseEnv[Eff], bizEnv: BizEnv[CtxEff]): WithProvide[CtxEff, Eff, Ctx[Eff]] = {
    new WithProvide[CtxEff, Eff, Ctx[Eff]] {
      def lift[A](fa: Eff[A]): CtxEff[A] = ContextT.liftF(fa)
      def runContext[A](fa: CtxEff[A])(ctx: env.Ctx[Eff]): Eff[A] =
        fa.run(CtxEnv(baseEnv.imapK(liftF)(runContextK(ctx)), bizEnv, ctx.imapK(liftF)(runContextK(ctx))))
    }
  }
}
