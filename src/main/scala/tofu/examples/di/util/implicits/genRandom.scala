package tofu.examples.di.util.implicits

import cats.FlatMap
import tofu.HasContext
import tofu.generate.GenRandom
import tofu.higherKind.Embed

object genRandom {
  implicit def contextEmbedGenRandom[F[_]: FlatMap](
    implicit FH: F HasContext GenRandom[F],
    UE: Embed[GenRandom]
  ): GenRandom[F] =
    UE.embed(FH.context)
}
