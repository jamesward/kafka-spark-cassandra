(managedClasspath in Runtime) += (WebKeys.webJarsDirectory in Assets).value

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.14",
  "org.apache.spark" %% "spark-sql" % "2.1.0",
  "org.apache.spark" %% "spark-streaming" % "2.1.0",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.1.0",
  "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0",
  "com.typesafe.play" %% "play-netty-server" % "2.5.13",
  "org.webjars" % "javascript-piano" % "74c90339ad1d55a72a99fe6e33b35752c15d71c7"
)

dependencyOverrides ++= Set("com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5")

val setupLogger = TaskKey[Unit]("setup-logger")

setupLogger := {
  org.apache.log4j.BasicConfigurator.configure()
  org.apache.log4j.Logger.getRootLogger.setLevel(org.apache.log4j.Level.INFO)
}

(WebKeys.webJars in Assets) := (WebKeys.webJars in Assets).dependsOn(setupLogger).value
