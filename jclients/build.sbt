libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "2.1.0",
  "org.apache.spark" %% "spark-streaming" % "2.1.0",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.1.0",
  "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0",
  "org.apache.kafka" % "kafka-streams" % "0.10.2.0" exclude("org.slf4j", "slf4j-simple"),
  "org.webjars" % "javascript-piano" % "74c90339ad1d55a72a99fe6e33b35752c15d71c7"
)

dependencyOverrides ++= Set("com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5")

//excludeDependencies += "ch.qos.logback" % "logback-classic"

excludeDependencies += "org.slf4j" % "slf4j-log4j12"
