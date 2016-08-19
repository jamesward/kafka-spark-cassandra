name := "kafka-spark-cassandra"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "0.10.0.1",
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.11-M4",
  "org.apache.cassandra" % "cassandra-all" % "3.7",
  "org.apache.spark" %% "spark-core" % "1.6.2",
  "org.apache.spark" %% "spark-sql" % "1.6.2",
  "com.datastax.spark" %% "spark-cassandra-connector" % "1.6.0",
  "org.http4s"       %% "http4s-dsl"          % "0.14.2a",
  "org.http4s"       %% "http4s-blaze-server" % "0.14.2a",
  "org.scalatest"    %% "scalatest"           % "2.2.6" % "test"
)

excludeDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic",
  "org.slf4j" % "log4j-over-slf4j"
)

val startKafka = TaskKey[Unit]("start-kafka")

startKafka := {
  (runMain in Compile).toTask(" KafkaServer").value
}

val startCassandra = taskKey[Unit]("start-cassandra")

startCassandra := {
  (runMain in Compile).toTask(" CassandraServer").value
}

cancelable in Global := true
