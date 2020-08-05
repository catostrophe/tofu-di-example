package tofu.examples.di.impls

import cats.Monad
import cats.effect.Resource
import cats.instances.boolean._
import cats.instances.string._
import io.janstenpickle.trace4cats.inject.Trace
import tofu.common.Console
import tofu.examples.di.algebras.{BusinessLogic, DB, HttpClient, Kafka, Security, Wrapper}
import tofu.examples.di.model.User
import tofu.examples.di.util.syntax.trace._
import tofu.syntax.console._
import tofu.syntax.monadic._

class ConcreteBusinessLogic[F[_]: Monad: HttpClient: Kafka: DB: Security: Console: Trace: Wrapper]
  extends BusinessLogic[F] {
  def process(id: Int): F[Unit] =
    for {
      resp <- Wrapper[F].wrap(HttpClient[F].send(s"i am request for $id").span("http-req"))("http-request")
      _ <- Kafka[F].send(s"resps for $id", resp).span("kafka-send")
      user <- DB[F].get(id).map(User.apply).span("db-get")
      auth <- Security[F].checkAuth(user, "i am okay haha not im kiddin'").span("sec-check")
      _ <- puts"user '$user' with auth '$auth' and responce is '$resp'".span("console-put")
    } yield ()
}
object ConcreteBusinessLogic {
  def make[
    I[_]: Monad,
    F[_]: Monad: HttpClient: Kafka: DB: Security: Console: Trace: Wrapper
  ]: Resource[I, BusinessLogic[F]] =
    new ConcreteBusinessLogic[F].pure[Resource[I, *]]
}
