import org.apache.spark.{SparkContext, SparkConf}
import com.datastax.spark.connector._

object HelloSpark extends App {

  val conf = new SparkConf(true).set("spark.cassandra.connection.host", "127.0.0.1")
  val sc = new SparkContext("local", "test", conf)

  val rdd = sc.cassandraTable("demo", "foo")
  println(rdd.count)
  println(rdd.first)
  println(rdd.map(_.getString("name")).collect().mkString(","))

  sc.stop()

}
