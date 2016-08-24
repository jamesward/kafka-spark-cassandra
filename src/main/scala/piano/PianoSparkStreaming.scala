package piano


import com.datastax.driver.core.ConsistencyLevel
import com.datastax.spark.connector.{AllColumns, ColumnName, SomeColumns}
import com.datastax.spark.connector.streaming._
import com.datastax.spark.connector.writer.WriteConf
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.util.Random



object PianoSparkStreaming extends App {

  val randClientId = Random.nextInt()

  val conf = new SparkConf(true).set("spark.cassandra.connection.host", "127.0.0.1")
  val context = new SparkContext("local", "PianoStreamingJob", conf)
  val streamingContext = new StreamingContext(context, Seconds(1))
  val session = SparkSession.builder.config(conf).getOrCreate()

  import session.implicits._

  val topic = "keycodes"
  val brokers = "localhost:9092"

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> brokers,
    "key.deserializer" -> classOf[IntegerDeserializer],
    "value.deserializer" -> classOf[IntegerDeserializer],
    "group.id" -> s"consumer-${Random.nextInt}-${System.currentTimeMillis}")

  val ls = LocationStrategies.PreferBrokers
  val cs = ConsumerStrategies.Subscribe[Integer, Integer](List(topic), kafkaParams)
  val rawKafkaStream = KafkaUtils.createDirectStream[Integer, Integer](streamingContext, ls, cs)

  val cassandraWriteConf = WriteConf.fromSparkConf(conf).copy(consistencyLevel = ConsistencyLevel.ONE)

  val jobStream = rawKafkaStream.map { consumerRecord =>
    PianoSong(randClientId, consumerRecord.key, Seq(consumerRecord.value))
  }


  val columnMapping = SomeColumns("client_id", "song_id", ColumnName("key_codes").append)
  jobStream.saveToCassandra("demo", "song", columnMapping, cassandraWriteConf)
  jobStream.foreachRDD(_.toDF().show())

  streamingContext.start()
  streamingContext.awaitTermination()

}
