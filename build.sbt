name := "kafka-spark-cassandra"

scalaVersion := "2.11.8"

val akkaStreamKafkaVersion = "0.13"
val cassandraVersion = "3.7"
val kafkaVersion = "0.10.1.1"
val playVersion = "2.5.12"
val scalatestVersion = "2.2.6"
val sparkVersion = "2.1.0"
val sparkCassandraConnectorVersion = "2.0.0-M3"
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
  "com.typesafe.play" %% "play-netty-server" % playVersion,
  "org.scalatest"    %% "scalatest"  % scalatestVersion % "test"
)

dependencyOverrides ++= Set("com.fasterxml.jackson.core" % "jackson-databind" % "2.6.6")

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
