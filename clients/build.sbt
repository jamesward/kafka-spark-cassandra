(managedClasspath in Runtime) += (WebKeys.webJarsDirectory in Assets).value

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.14",
  "org.apache.spark" %% "spark-sql" % "2.1.0",
  "org.apache.spark" %% "spark-streaming" % "2.1.0",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.1.0",
  "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0",
  "com.typesafe.play" %% "play-netty-server" % "2.5.13"
)

dependencyOverrides ++= Set("com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5")
