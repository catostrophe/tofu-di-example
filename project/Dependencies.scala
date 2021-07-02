import sbt._

object Dependencies {

  object Versions {
    val cats = "2.4.2"
    val catsEffect = "2.3.3"
    val catsTagless = "0.12"
    val derevo = "0.12.1"
    val tofu = "0.10.2"
    val trace4cats = "0.9.0"

    val logback = "1.2.3"

    val kindProjector = "0.11.3"
    val betterMonadicFor = "0.3.1"
  }

  val catsCore = "org.typelevel" %% "cats-core" % Versions.cats
  val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
  val catsTaglessCore = "org.typelevel" %% "cats-tagless-core" % Versions.catsTagless
  val catsTaglessMacros = "org.typelevel" %% "cats-tagless-macros" % Versions.catsTagless
  val derevoCats = "tf.tofu" %% "derevo-cats" % Versions.derevo
  val derevoCatsTagless = "tf.tofu" %% "derevo-cats-tagless" % Versions.derevo
  val tofuCore = "tf.tofu" %% "tofu-core" % Versions.tofu
  val tofuConcurrent = "tf.tofu" %% "tofu-concurrent" % Versions.tofu
  val tofuDerivation = "tf.tofu" %% "tofu-derivation" % Versions.tofu
  val tofuOpticsCore = "tf.tofu" %% "tofu-optics-core" % Versions.tofu
  val tofuOpticsMacro = "tf.tofu" %% "tofu-optics-macro" % Versions.tofu
  val trace4catsCore = "io.janstenpickle" %% "trace4cats-core" % Versions.trace4cats
  val trace4catsInject = "io.janstenpickle" %% "trace4cats-inject" % Versions.trace4cats
  val trace4catsJaeger = "io.janstenpickle" %% "trace4cats-jaeger-thrift-exporter" % Versions.trace4cats

  val logback = "ch.qos.logback" % "logback-classic" % Versions.logback

  val kindProjector = "org.typelevel" % "kind-projector" % Versions.kindProjector cross CrossVersion.patch
  val betterMonadicFor = "com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor
}
