package tofu.examples.di.util

trait Summoner[U[_[_]]] {
  def apply[F[_]](implicit ev: U[F]): U[F] = ev
}
