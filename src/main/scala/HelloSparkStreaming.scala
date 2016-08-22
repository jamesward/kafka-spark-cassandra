
import com.datastax.driver.core.ConsistencyLevel
import com.datastax.spark.connector.writer.WriteConf
import org.apache.kafka.common.serialization.{IntegerDeserializer, StringDeserializer}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Time, Seconds, StreamingContext}
import org.apache.spark.SparkConf
import scala.util.Random
import com.datastax.spark.connector.streaming._

case class RandomJob(job_id: Int, count:Int, rand_int: Int)

object HelloSparkStreaming extends App {

  val rand_job_id = Random.nextInt()

  val conf = new SparkConf(true)
    .set("spark.cassandra.connection.host", "127.0.0.1")
    .setMaster("local")
    .setAppName("RandomJob")

  val sparkStreamingContext = new StreamingContext(conf, Seconds(1))

  val topic = "RandomNumbers"
  val brokers = "localhost:9092"

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> brokers,
    "key.deserializer" -> classOf[IntegerDeserializer],
    "value.deserializer" -> classOf[IntegerDeserializer],
    "group.id" -> s"consumer-${Random.nextInt}-${System.currentTimeMillis}")


  val ls = LocationStrategies.PreferBrokers
  val cs = ConsumerStrategies.Subscribe[Integer, Integer](List(topic), kafkaParams)
  val rawKafkaStream = KafkaUtils
    .createDirectStream[Integer, Integer](sparkStreamingContext, ls, cs)

  private val sparkConf = sparkStreamingContext.sparkContext.getConf
  private val cassandraWriteConf: WriteConf = WriteConf.fromSparkConf(sparkConf).copy(consistencyLevel = ConsistencyLevel.ONE)


  val spark = SparkSession.builder.config(sparkConf).getOrCreate()
  import spark.implicits._

  val jobStream = rawKafkaStream
    .map { consumerRecord =>
      RandomJob(rand_job_id, consumerRecord.key, consumerRecord.value)
    }

    jobStream
      .saveToCassandra("demo", "rand_ints", writeConf = cassandraWriteConf)

    jobStream.foreachRDD {
      (nxtRandomJob: RDD[RandomJob], batchTime: Time) =>
        val nextJobDF = nxtRandomJob.toDF()
         nextJobDF.show()
    }



  //Kick off
  sparkStreamingContext.start()
  sparkStreamingContext.awaitTermination()


}
