import Dependencies._

name := "tofu-di-example"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
    catsCore,
    catsEffect,
    catsTaglessCore,
    catsTaglessMacros,
    derevoCats,
    derevoCatsTagless,
    tofuCore,
    tofuConcurrent,
    tofuDerivation,
    tofuOpticsCore,
    tofuOpticsMacro,
    trace4catsCore,
    trace4catsInject,
    trace4catsJaeger,
    logback,
    compilerPlugin(kindProjector),
    compilerPlugin(betterMonadicFor),
  )

scalacOptions ++= List("-Ymacro-annotations", "-Wconf:any:wv")
//scalacOptions ~= filterConsoleScalacOptions //disable some strict lints

Global / cancelable := true
run / fork := true

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt it:scalafmt")
addCommandAlias("checkfmt", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck it:scalafmtCheck")
addCommandAlias("deps", "dependencyUpdates")
