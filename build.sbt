name := "kafka-spark-cassandra"

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8"
)

lazy val kafkaServer = (project in file("kafka-server")).settings(commonSettings: _*)

lazy val cassandraServer = (project in file("cassandra-server")).settings(commonSettings: _*)

lazy val sclients = (project in file("sclients")).settings(commonSettings: _*).enablePlugins(SbtWeb)

lazy val jclients = (project in file("jclients")).settings(commonSettings: _*).enablePlugins(PlayJava).disablePlugins(PlayLogback)

TaskKey[Unit]("startCassandra") := (runMain in Compile in cassandraServer).toTask(" CassandraServer").value
TaskKey[Unit]("startKafka") := (runMain in Compile in kafkaServer).toTask(" KafkaServer").value

TaskKey[Unit]("sHelloCassandra") := (runMain in Compile in sclients).toTask(" HelloCassandra").value
TaskKey[Unit]("sHelloKafka") := (runMain in Compile in sclients).toTask(" HelloKafka").value
TaskKey[Unit]("sHelloPlay") := (runMain in Compile in sclients).toTask(" HelloPlay").value
TaskKey[Unit]("sHelloSpark") := (runMain in Compile in sclients).toTask(" HelloSpark").value
TaskKey[Unit]("sHelloSparkStreaming") := (runMain in Compile in sclients).toTask(" HelloSparkStreaming").value
TaskKey[Unit]("sSetupPiano") := (runMain in Compile in sclients).toTask(" piano.CassandraSetup").value
TaskKey[Unit]("sPianoSparkStreaming") := (runMain in Compile in sclients).toTask(" piano.SparkStreaming").value
TaskKey[Unit]("sStartWebServer") := (runMain in Compile in sclients).toTask(" piano.WebServer").value

InputKey[Unit]("jHelloPlay") := (run in Compile in jclients).evaluated
TaskKey[Unit]("jHelloCassandra") := (runMain in Compile in jclients).toTask(" HelloCassandra").value
TaskKey[Unit]("jHelloKafka") := (runMain in Compile in jclients).toTask(" HelloKafka").value
TaskKey[Unit]("jSetupPiano") := (runMain in Compile in jclients).toTask(" piano.CassandraSetup").value
TaskKey[Unit]("jPianoSparkStreaming") := (runMain in Compile in jclients).toTask(" piano.SparkStreaming").value

enablePlugins(AtomPlugin)
