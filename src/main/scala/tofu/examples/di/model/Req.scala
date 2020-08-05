package tofu.examples.di.model

import cats.tagless.FunctorK
import cats.~>

final case class Req[F[_]](header: String, body: F[String])
object Req {
  implicit val reqFunctorK: FunctorK[Req] = new FunctorK[Req] {
    def mapK[F[_], G[_]](af: Req[F])(fk: F ~> G): Req[G] = Req(af.header, fk(af.body))
  }
}
