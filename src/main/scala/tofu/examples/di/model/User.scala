package tofu.examples.di.model

import cats.instances.string._
import derevo.cats.show
import derevo.derive

@derive(show)
final case class User(name: String)
