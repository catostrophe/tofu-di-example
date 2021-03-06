package tofu.examples.di.util.implicits

import cats.FlatMap
import cats.effect.Resource
import io.janstenpickle.trace4cats.{ErrorHandler, Span}
import io.janstenpickle.trace4cats.inject.EntryPoint
import io.janstenpickle.trace4cats.model.{SpanKind, TraceHeaders}
import tofu.HasContext
import tofu.higherKind.Embed
import tofu.syntax.monadic._

object entryPoint {
  final implicit val entryPointEmbed: Embed[EntryPoint] =
    new Embed[EntryPoint] {
      def embed[F[_]](ft: F[EntryPoint[F]])(implicit evidence$1: FlatMap[F]): EntryPoint[F] =
        new EntryPoint[F] {
          def root(name: String, kind: SpanKind, errorHandler: ErrorHandler): Resource[F, Span[F]] =
            Resource.suspend(ft.map(_.root(name, kind, errorHandler)))
          def continueOrElseRoot(
            name: String,
            kind: SpanKind,
            headers: TraceHeaders,
            errorHandler: ErrorHandler
          ): Resource[F, Span[F]] =
            Resource.suspend(ft.map(_.continueOrElseRoot(name, kind, headers, errorHandler)))
        }
    }

  final implicit def contextEmbedEntryPoint[F[_]: FlatMap](
    implicit
    FH: F HasContext EntryPoint[F],
    UE: Embed[EntryPoint]
  ): EntryPoint[F] =
    UE.embed(FH.context)
}
