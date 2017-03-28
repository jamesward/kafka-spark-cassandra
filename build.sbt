name := "kafka-spark-cassandra"

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8"
)

lazy val kafkaServer = (project in file("kafka-server")).settings(commonSettings: _*)

lazy val cassandraServer = (project in file("cassandra-server")).settings(commonSettings: _*)

lazy val clients = (project in file("clients")).settings(commonSettings: _*).enablePlugins(SbtWeb)

enablePlugins(AtomPlugin)
