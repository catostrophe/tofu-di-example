package tofu.examples.di.util.implicits

import cats.Defer
import io.janstenpickle.trace4cats.inject.Trace
import io.janstenpickle.trace4cats.model.{AttributeValue, SpanKind, SpanStatus}
import io.janstenpickle.trace4cats.{Span, ToHeaders}
import tofu.syntax.context._
import tofu.syntax.monadic._
import tofu.{BracketThrow, WithLocal}

object trace {
  implicit def localSpanTrace[F[_]: BracketThrow: Defer](implicit C: WithLocal[F, Span[F]]): Trace[F] =
    new Trace[F] {
      def put(key: String, value: AttributeValue): F[Unit] = C.askF(_.put(key, value))
      def putAll(fields: (String, AttributeValue)*): F[Unit] = C.askF(_.putAll(fields: _*))
      def span[A](name: String, kind: SpanKind)(fa: F[A]): F[A] =
        C.askF(_.child(name, kind).use(child => fa.local(_ => child)))
      def headers(toHeaders: ToHeaders): F[Map[String, String]] =
        C.askF(span => toHeaders.fromContext(span.context).pure[F])
      def setStatus(status: SpanStatus): F[Unit] = C.askF(_.setStatus(status))
    }
}
