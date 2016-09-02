package piano

import akka.stream.scaladsl.{Flow, Source}
import controllers.Assets
import play.api.libs.json._
import play.api.mvc.{Action, Results, WebSocket}
import play.api.routing.Router
import play.api.routing.sird._
import play.api.{BuiltInComponents, Mode}
import play.core.server.{NettyServerComponents, ServerConfig}

import scala.concurrent.Future


object WebServer extends App {

  val components = new NettyServerComponents with BuiltInComponents {

    def port() = sys.env.getOrElse("PORT", "8080").toInt

    def mode() = if (configuration.getString("play.crypto.secret").contains("changeme")) Mode.Dev else Mode.Prod

    lazy val kafkaHelper = KafkaHelper(actorSystem, materializer)

    override lazy val serverConfig = ServerConfig(port = Some(port()), mode = mode())

    override lazy val router = Router.from {
      case GET(p"/") => Assets.at("/public", "index.html")
      case GET(p"/lib/$path*") => Assets.at("/lib", path)
    }
  }

  val server = components.server
  while (!Thread.currentThread.isInterrupted) {}
  server.stop()
  CassandraHelper.closeConnection()
}
