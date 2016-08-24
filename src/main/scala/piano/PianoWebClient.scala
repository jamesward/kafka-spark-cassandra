package piano

import java.util.concurrent.atomic.AtomicInteger

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.config.ConfigFactory
import controllers.Assets
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.IntegerSerializer
import play.api.libs.json.Json
import play.api.mvc.{Action, Results}
import play.api.routing.Router
import play.api.routing.sird._
import play.api.{BuiltInComponents, Mode}
import play.core.server.{NettyServerComponents, ServerConfig}

import scala.concurrent.Future

object PianoWebClient extends App {



  val components = new NettyServerComponents with BuiltInComponents {

    def port() = sys.env.getOrElse("PORT", "8080").toInt

    def mode() = if (configuration.getString("play.crypto.secret").contains("changeme")) Mode.Dev else Mode.Prod

    override lazy val serverConfig = ServerConfig(port = Some(port()), mode = mode())

    lazy val router = Router.from {
      case GET(p"/piano") =>
        Assets.at(path = "/piano", file = "index.html")
      case GET(p"/assets/$file*") =>
        Assets.at(path = "/piano", file = file)
      case GET(p"/keycodes" ? q"songId=$songId" & q"keyCode=$keyCode") => Action {
        produceKeyCodeMessage(songId.toInt, keyCode.toInt)
        Results.Ok("")
      }
      case GET(p"/keycodes" ? q"clientId=$clientId" & q"songId=$songId") => Action {
        val song: PianoSong = PianoCassandraHelper.pianoCassandraHelper.getPianoSong(clientId.toInt, songId.toInt)
        Results.Ok(song.key_codes.mkString(","))
      }
    }
  }

  val server = components.server
  while (!Thread.currentThread.isInterrupted) {}
  server.stop()

  import KafkaPianoConfig._

  def produceKeyCodeMessage(songId:Int, keyCode: Int) = {
    val sink: Sink[ProducerRecord[Integer, Integer], Future[Done]] = Producer.plainSink(producerSettings)
    val keySource = Source.single(new ProducerRecord[Integer, Integer](topic, songId, keyCode) )

    keySource
      .to(sink)
      .run()
  }
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
