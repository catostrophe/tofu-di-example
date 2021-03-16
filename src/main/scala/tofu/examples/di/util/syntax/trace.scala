package tofu.examples.di.util.syntax

import cats.effect.Resource
import cats.{Applicative, Defer, ~>}
import io.janstenpickle.trace4cats.{ErrorHandler, Span}
import io.janstenpickle.trace4cats.inject.{EntryPoint, Trace}
import io.janstenpickle.trace4cats.model.{SpanKind, TraceHeaders}
import tofu.lift.Lift

object trace {
  implicit final class TraceOps[F[_], A](private val fa: F[A]) extends AnyVal {
    def span(operationName: String)(implicit T: Trace[F]): F[A] =
      T.span(operationName)(fa)
  }

  implicit final class EntryPointOps[F[_]](private val ep: EntryPoint[F]) extends AnyVal {
    def mapK[G[_]: Defer: Applicative](fk: F ~> G): EntryPoint[G] =
      new EntryPoint[G] {
        def root(name: String, kind: SpanKind, errorHandler: ErrorHandler): Resource[G, Span[G]] =
          ep.root(name, kind, errorHandler).mapK(fk).map(_.mapK(fk))
        def continueOrElseRoot(
          name: String,
          kind: SpanKind,
          headers: TraceHeaders,
          errorHandler: ErrorHandler
        ): Resource[G, Span[G]] =
          ep.continueOrElseRoot(name, kind, headers, errorHandler).mapK(fk).map(_.mapK(fk))
      }
    def lift[G[_]: Defer: Applicative](implicit L: Lift[F, G]): EntryPoint[G] = mapK(L.liftF)
  }

}
