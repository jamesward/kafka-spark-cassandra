import java.util.UUID

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{ConsumerSettings, ProducerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

import scala.concurrent.duration._
import scala.util.Random

object HelloKafka extends App {

  implicit val actorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val bootstrapServer = "localhost:9092"
  val topic = "RandomNumbers"


  // Consume Messages
  val consumerConfig = ConfigFactory.defaultReference().getConfig("akka.kafka.consumer")
  val deserializer = new StringDeserializer()
  val consumerSettings = ConsumerSettings[String, String](consumerConfig, deserializer, deserializer)
    .withBootstrapServers(bootstrapServer)
    .withGroupId(UUID.randomUUID().toString)

  val subscription = Subscriptions.topics(topic)
  Consumer.plainSource(consumerSettings, subscription).runForeach(consumerRecord => println(consumerRecord.value()))


  // Produce Message
  val producerConfig = ConfigFactory.defaultReference().getConfig("akka.kafka.producer")
  val serializer = new StringSerializer()
  val producerSettings = ProducerSettings[String, String](producerConfig, serializer, serializer)
    .withBootstrapServers(bootstrapServer)

  val sink: Sink[ProducerRecord[String, String], NotUsed] = Producer.plainSink(producerSettings)
  val tickSource = Source.tick(Duration.Zero, 500.milliseconds, Unit).map(_ => Random.nextInt().toString)

  tickSource
    .map(new ProducerRecord[String, String](topic, _))
    .to(sink)
    .run()

}
