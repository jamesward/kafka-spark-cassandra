package piano

import com.datastax.driver.core.ConsistencyLevel
import com.datastax.spark.connector.streaming._
import com.datastax.spark.connector.writer.WriteConf
import com.datastax.spark.connector.{ColumnName, SomeColumns}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object SparkStreaming extends App {

  val conf = new SparkConf(true).set("spark.cassandra.connection.host", CassandraHelper.host)
  conf.set("spark.sql.warehouse.dir", "spark-warehouse")
  val context = new SparkContext("local", "PianoStreamingJob", conf)
  val streamingContext = new StreamingContext(context, Seconds(1))
  val session = SparkSession.builder.config(conf).getOrCreate()

  import session.implicits._

  val ls = LocationStrategies.PreferBrokers
  val cs = ConsumerStrategies.Subscribe[String, Integer](List(KafkaHelper.recordTopic), KafkaHelper.kafkaParams("group"))
  val rawKafkaStream = KafkaUtils.createDirectStream[String, Integer](streamingContext, ls, cs)

  val jobStream = rawKafkaStream.map(KafkaHelper.toPianoSong)

  val columnMapping = SomeColumns("song_id", ColumnName("key_codes").append)
  val cassandraWriteConf = WriteConf.fromSparkConf(conf).copy(consistencyLevel = ConsistencyLevel.ONE)
  jobStream.saveToCassandra("demo", "song", columnMapping, cassandraWriteConf)
  jobStream.foreachRDD(_.toDF().show())

  streamingContext.start()
  streamingContext.awaitTermination()

}
