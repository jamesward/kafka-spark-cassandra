name := "kafka-spark-cassandra"

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8"
)

lazy val kafkaServer = (project in file("kafka-server")).settings(commonSettings: _*)

lazy val cassandraServer = (project in file("cassandra-server")).settings(commonSettings: _*)

lazy val clients = (project in file("clients")).settings(commonSettings: _*).enablePlugins(SbtWeb)

TaskKey[Unit]("startCassandra") := (runMain in Compile in cassandraServer).toTask(" CassandraServer").value
TaskKey[Unit]("startKafka") := (runMain in Compile in kafkaServer).toTask(" KafkaServer").value
TaskKey[Unit]("helloCassandra") := (runMain in Compile in clients).toTask(" HelloCassandra").value
TaskKey[Unit]("helloKafka") := (runMain in Compile in clients).toTask(" HelloKafka").value
TaskKey[Unit]("helloPlay") := (runMain in Compile in clients).toTask(" HelloPlay").value
TaskKey[Unit]("helloSpark") := (runMain in Compile in clients).toTask(" HelloSpark").value
TaskKey[Unit]("helloSparkStreaming") := (runMain in Compile in clients).toTask(" HelloSparkStreaming").value
TaskKey[Unit]("setupPiano") := (runMain in Compile in clients).toTask(" piano.CassandraSetup").value
TaskKey[Unit]("pianoSparkStreaming") := (runMain in Compile in clients).toTask(" piano.SparkStreaming").value
TaskKey[Unit]("startWebServer") := (runMain in Compile in clients).toTask(" piano.WebServer").value

enablePlugins(AtomPlugin)
