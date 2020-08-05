package tofu.examples.di.impls

import cats.{Applicative, Defer}
import cats.effect.{Blocker, Concurrent, ContextShift, Resource, Timer}
import io.janstenpickle.trace4cats.inject.EntryPoint
import io.janstenpickle.trace4cats.jaeger.JaegerSpanCompleter
import io.janstenpickle.trace4cats.kernel.SpanSampler
import io.janstenpickle.trace4cats.model.TraceProcess
import tofu.examples.di.util.syntax.trace._
import tofu.lift.Lift

object JaegerEntryPoint {
  def make[I[_]: Concurrent: ContextShift: Timer, F[_]: Defer: Applicative: Lift[I, *[_]]]: Resource[I, EntryPoint[F]] =
    for {
      blocker <- Blocker[I]
      process = TraceProcess("tofu-di-example")
      completer <- JaegerSpanCompleter[I](blocker, process, host = "localhost", port = 6831)
      entryPoint = EntryPoint[I](SpanSampler.probabilistic[I](1), completer)
    } yield entryPoint.lift[F]
}
