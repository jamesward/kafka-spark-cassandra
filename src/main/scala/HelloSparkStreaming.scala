import com.datastax.driver.core.ConsistencyLevel
import com.datastax.spark.connector.AllColumns
import com.datastax.spark.connector.streaming._
import com.datastax.spark.connector.writer.WriteConf
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Random

case class RandomJob(job_id: Int, count: Int, rand_int: Int)

object HelloSparkStreaming extends App {

  val randJobId = Random.nextInt()

  val conf = new SparkConf(true).set("spark.cassandra.connection.host", "127.0.0.1")
  val context = new SparkContext("local", "RandomJob", conf)
  val streamingContext = new StreamingContext(context, Seconds(1))
  val session = SparkSession.builder.config(conf).getOrCreate()

  import session.implicits._

  val topic = "RandomNumbers"
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
    RandomJob(randJobId, consumerRecord.key, consumerRecord.value)
  }

  jobStream.saveToCassandra("demo", "rand_ints", AllColumns, cassandraWriteConf)
  jobStream.foreachRDD(_.toDF().show())

  streamingContext.start()
  streamingContext.awaitTermination()

}
