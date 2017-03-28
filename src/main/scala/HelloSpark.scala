import com.datastax.spark.connector._
import org.apache.spark.{SparkConf, SparkContext}

object HelloSpark extends App {

  val conf = new SparkConf(true).set("spark.cassandra.connection.host", "127.0.0.1")
  val sc = new SparkContext("local", "test", conf)

  println("\n\n  ### BEGIN DEMO DATA ### ")

  val rdd = sc.cassandraTable("demo", "foo")
  println(s"\n### -> Table row count: ${rdd.count}\n")
  println(s"\n### -> First row: ${rdd.first}\n")

  private val fooNames: String = rdd.map(_.getString("name")).collect().mkString(",")
  println(s"\n### -> FOO names: $fooNames\n")

  sc.stop()

}
