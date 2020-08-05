package tofu.examples.di.util.implicits

import cats.FlatMap
import cats.effect.{Clock, Timer}
import tofu.HasContext
import tofu.higherKind.Embed

object timer {

  implicit val clockEmbed: Embed[Clock] = tofu.higherKind.derived.genEmbed[Clock]
  implicit val timerEmbed: Embed[Timer] = tofu.higherKind.derived.genEmbed[Timer]

  implicit def contextEmbedTimer[F[_]: FlatMap](implicit FH: F HasContext Timer[F], UE: Embed[Timer]): Timer[F] =
    UE.embed(FH.context)
}
