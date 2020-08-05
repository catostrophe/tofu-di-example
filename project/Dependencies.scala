import sbt._

object Dependencies {

  object Versions {
    val cats = "2.1.1"
    val catsEffect = "2.1.4"
    val catsTagless = "0.11"
    val derevo = "0.11.4"
    val tofu = "0.7.8"
    val trace4cats = "0.3.0"

    val logback = "1.2.3"

    val kindProjector = "0.11.0"
    val betterMonadicFor = "0.3.1"
  }

  val catsCore = "org.typelevel" %% "cats-core" % Versions.cats
  val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
  val catsTaglessCore = "org.typelevel" %% "cats-tagless-core" % Versions.catsTagless
  val catsTaglessMacros = "org.typelevel" %% "cats-tagless-macros" % Versions.catsTagless
  val derevoCats = "org.manatki" %% "derevo-cats" % Versions.derevo
  val derevoCatsTagless = "org.manatki" %% "derevo-cats-tagless" % Versions.derevo
  val tofuCore = "ru.tinkoff" %% "tofu-core" % Versions.tofu
  val tofuConcurrent = "ru.tinkoff" %% "tofu-concurrent" % Versions.tofu
  val tofuDerivation = "ru.tinkoff" %% "tofu-derivation" % Versions.tofu
  val tofuOpticsCore = "ru.tinkoff" %% "tofu-optics-core" % Versions.tofu
  val tofuOpticsMacro = "ru.tinkoff" %% "tofu-optics-macro" % Versions.tofu
  val trace4catsCore = "io.janstenpickle" %% "trace4cats-core" % Versions.trace4cats
  val trace4catsInject = "io.janstenpickle" %% "trace4cats-inject" % Versions.trace4cats
  val trace4catsJaeger = "io.janstenpickle" %% "trace4cats-jaeger-thrift-exporter" % Versions.trace4cats

  val logback = "ch.qos.logback" % "logback-classic" % Versions.logback

  val kindProjector = "org.typelevel" % "kind-projector" % Versions.kindProjector cross CrossVersion.patch
  val betterMonadicFor = "com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor
}
