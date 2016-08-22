import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{ConsumerSettings, ProducerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{IntegerDeserializer, IntegerSerializer, StringDeserializer, StringSerializer}

import scala.concurrent.duration._
import scala.util.Random

object HelloKafka extends App {

  implicit val actorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val bootstrapServer = "localhost:9092"
  val topic = "RandomNumbers"


  // Consume Messages
  val consumerConfig = ConfigFactory.defaultReference().getConfig("akka.kafka.consumer")
  val deserializer = new IntegerDeserializer()
  val consumerSettings = ConsumerSettings[Integer, Integer](consumerConfig, deserializer, deserializer)
    .withBootstrapServers(bootstrapServer)
    .withGroupId(UUID.randomUUID().toString)

  val subscription = Subscriptions.topics(topic)
  Consumer
    .plainSource(consumerSettings, subscription)
    .runForeach(consumerRecord => println(s"${consumerRecord.key}:${consumerRecord.value()}"))


  // Produce Message
  val count = new AtomicInteger()
  val producerConfig = ConfigFactory.defaultReference().getConfig("akka.kafka.producer")
  val serializer = new IntegerSerializer()
  val producerSettings = ProducerSettings[Integer, Integer](producerConfig, serializer, serializer)
    .withBootstrapServers(bootstrapServer)

  val sink: Sink[ProducerRecord[Integer, Integer], NotUsed] = Producer.plainSink(producerSettings)
  val tickSource = Source.tick(Duration.Zero, 500.milliseconds, Unit).map(_ => Random.nextInt())

  tickSource
    .map(new ProducerRecord[Integer, Integer](topic, count.getAndIncrement(), _))
    .to(sink)
    .run()

}
