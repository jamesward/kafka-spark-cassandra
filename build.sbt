name := "kafka-spark-cassandra"

scalaVersion := "2.11.8"

val kafkaVersion = "0.10.0.1"
val akkaStreamKafkaVersion = "0.11-RC1"
val cassandraVersion = "3.7"
val sparkVersion = "2.0.0"
val sparkCassandraConnectorVersion = "2.0.0-M1"
val http4s = "0.14.2a"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % kafkaVersion,
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  "com.typesafe.akka" %% "akka-stream-kafka" % akkaStreamKafkaVersion,
  "org.apache.cassandra" % "cassandra-all" % cassandraVersion,
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,
  "com.datastax.spark" %% "spark-cassandra-connector" % sparkCassandraConnectorVersion,
  "org.http4s"       %% "http4s-dsl"          % http4s,
  "org.http4s"       %% "http4s-blaze-server" % http4s,
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
