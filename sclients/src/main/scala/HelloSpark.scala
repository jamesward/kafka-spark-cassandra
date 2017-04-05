import org.apache.spark.sql.SparkSession

object HelloSpark extends App {

  val spark = SparkSession.builder().config("spark.master", "local").getOrCreate()

  val df = spark.sqlContext.read.format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "demo", "table" -> "foo")).load()

  df.show()

  spark.stop()

}
