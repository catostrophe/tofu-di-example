package tofu.examples.di.config

import cats.Monad
import tofu.examples.di.config
import tofu.examples.di.config.Config._
import tofu.optics.macros.ClassyOptics
import tofu.syntax.monadic._

@ClassyOptics
case class Config(http: HttpConfig, db: DBConfig, kafka: KafkaConfig)

object Config {
  def load[I[_]: Monad]: I[Config] =
    config
      .Config(HttpConfig("http://u.rl"), DBConfig("jdbc://post.sql"), KafkaConfig("http://ka.fka", "qwerty")).pure[I]

  case class HttpConfig(url: String)

  case class DBConfig(connectionAddress: String)

  case class KafkaConfig(url: String, pass: String)
}
