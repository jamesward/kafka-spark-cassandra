package piano

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{ConsumerSettings, ProducerSettings, Subscriptions}
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{IntegerDeserializer, IntegerSerializer, StringDeserializer, StringSerializer}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future

case class KafkaHelper(actorSystem: ActorSystem, materializer: Materializer) {
  val stringSerializer = new StringSerializer()
  val integerSerializer = new IntegerSerializer()
  val producerConfig = ConfigFactory.load().getConfig("akka.kafka.producer")
  val producerSettings = ProducerSettings[String, Integer](producerConfig, stringSerializer, integerSerializer).withBootstrapServers(KafkaHelper.bootstrapServer)
  def sink(): Sink[ProducerRecord[String, Integer], Future[Done]] = Producer.plainSink(producerSettings)

  val stringDeserializer = new StringDeserializer()
  val integerDeserializer = new IntegerDeserializer()
  val consumerConfig = ConfigFactory.load().getConfig("akka.kafka.consumer")
  val consumerSettings = ConsumerSettings(consumerConfig, stringDeserializer, integerDeserializer).withBootstrapServers(KafkaHelper.bootstrapServer)
  def source(groupId: String, topic: String) = Consumer.plainSource(consumerSettings.withGroupId(groupId), Subscriptions.topics(topic))
}

object KafkaHelper {

  val bootstrapServer = "localhost:9092"
  val recordTopic = "piano.record"

  def kafkaParams(groupId: String) = Map[String, Object](
    "bootstrap.servers" -> KafkaHelper.bootstrapServer,
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[IntegerDeserializer],
    "group.id" -> groupId)

  def fromJson(topic: String)(jsValue: JsValue): ProducerRecord[String, Integer] = {
    val songId = (jsValue \ "songId").as[String]
    val keyCode = (jsValue \ "keyCode").asOpt[Int].getOrElse(Int.MaxValue)
    producerRecord(topic)(songId, keyCode)
  }

  def producerRecord(topic: String)(songId: String, keyCode: Integer) = {
    new ProducerRecord(topic, songId, keyCode)
  }

  def toJson(songId: String, keyCode: Int): JsValue = {
    Json.obj("songId" -> songId, "keyCode" -> keyCode)
  }

  def toJson(consumerRecord: ConsumerRecord[String, Integer]): JsValue = {
    toJson(consumerRecord.key(),consumerRecord.value().toInt)
  }

  def toPianoSong(consumerRecord: ConsumerRecord[String, Integer]): PianoSong = {
    PianoSong(consumerRecord.key, Seq(consumerRecord.value))
  }

}
