import org.http4s._
import org.http4s.dsl._
import org.http4s.server.blaze._


object HelloHttp4s extends App {

  val service = HttpService {
    case GET -> Root =>
      Ok("hello, world")
  }

  val builder = BlazeBuilder.bindHttp().mountService(service)
  val server = builder.run

  while (!Thread.currentThread.isInterrupted) {}

  server.shutdownNow()

}
