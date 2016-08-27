package piano

import java.util.concurrent.atomic.AtomicInteger

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.typesafe.config.ConfigFactory
import controllers.Assets
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.IntegerSerializer
import play.api.libs.json.{JsNull, Json}
import play.api.mvc.{Action, Results, WebSocket}
import play.api.routing.Router
import play.api.routing.sird._
import play.api.{BuiltInComponents, Mode}
import play.core.server.{NettyServerComponents, ServerConfig}

import scala.concurrent.Future

object PianoWebClient extends App {

  import KafkaPianoConfig._

  /*
  def produceKeyCodeMessage(songId:Int, keyCode: Int) = {
    val sink: Sink[ProducerRecord[Integer, Integer], Future[Done]] = Producer.plainSink(producerSettings)
    val keySource = Source.single(new ProducerRecord[Integer, Integer](topic, songId, keyCode) )

    keySource
      .to(sink)
      .run()
  }
  */

  val components = new NettyServerComponents with BuiltInComponents {

    def port() = sys.env.getOrElse("PORT", "8080").toInt

    def mode() = if (configuration.getString("play.crypto.secret").contains("changeme")) Mode.Dev else Mode.Prod

    override lazy val serverConfig = ServerConfig(port = Some(port()), mode = mode())

    override lazy val router = Router.from {
      case GET(p"/") => Assets.at("/public", "index.html")
      case GET(p"/lib/$path*") => Assets.at("/lib", path)
      case GET(p"/ws") => WebSocket.accept[String, String] { _ =>
        // todo: sink = transform a WS message and wire it to the kafka producer
        // todo: source = Maybe use an ActorProducer so that we can feed the 5 second song down to the client
        // val song: PianoSong = PianoCassandraHelper.pianoCassandraHelper.getPianoSong(clientId.toInt, songId.toInt)
        Flow.fromSinkAndSource(Sink.foreach[String](println), Source.maybe[String])
      }
    }
  }

  val server = components.server
  while (!Thread.currentThread.isInterrupted) {}
  server.stop()

}

case object KafkaPianoConfig {
  val count = new AtomicInteger()
  implicit val actorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()
  val bootstrapServer = "localhost:9092"
  val topic = "keycodes"

  val producerConfig = ConfigFactory.defaultReference().getConfig("akka.kafka.producer")
  val serializer = new IntegerSerializer()
  val producerSettings = ProducerSettings[Integer, Integer](producerConfig, serializer, serializer)
    .withBootstrapServers(bootstrapServer)
}
