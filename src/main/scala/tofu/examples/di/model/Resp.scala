package tofu.examples.di.model

import cats.tagless.FunctorK
import cats.~>

final case class Resp[F[_]](header: String, body: F[String])
object Resp {
  implicit val respFunctorK: FunctorK[Resp] = new FunctorK[Resp] {
    def mapK[F[_], G[_]](af: Resp[F])(fk: F ~> G): Resp[G] = Resp(af.header, fk(af.body))
  }
}
