package tofu.examples.di.util.implicits

import cats.Defer
import cats.syntax.option._
import cats.syntax.show._
import io.janstenpickle.trace4cats.inject.Trace
import io.janstenpickle.trace4cats.model.{AttributeValue, SpanKind, SpanStatus, TraceHeaders}
import io.janstenpickle.trace4cats.{ErrorHandler, Span, ToHeaders}
import tofu.syntax.context._
import tofu.syntax.monadic._
import tofu.{BracketThrow, WithLocal}

object trace {
  implicit def localSpanTrace[F[_]: BracketThrow: Defer](implicit C: WithLocal[F, Span[F]]): Trace[F] =
    new Trace[F] {
      def put(key: String, value: AttributeValue): F[Unit] = C.askF(_.put(key, value))
      def putAll(fields: (String, AttributeValue)*): F[Unit] = C.askF(_.putAll(fields: _*))
      def span[A](name: String, kind: SpanKind, errorHandler: ErrorHandler)(fa: F[A]): F[A] =
        C.askF(_.child(name, kind, errorHandler).use(child => fa.local(_ => child)))
      def headers(toHeaders: ToHeaders): F[TraceHeaders] =
        C.askF(span => toHeaders.fromContext(span.context).pure[F])
      def setStatus(status: SpanStatus): F[Unit] = C.askF(_.setStatus(status))
      def traceId: F[Option[String]] = C.ask(_.context.traceId.show.some)
    }
}
