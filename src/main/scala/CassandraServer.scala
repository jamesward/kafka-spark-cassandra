import java.nio.file.Files

import org.apache.cassandra.service.CassandraDaemon

object CassandraServer extends App {

  System.setProperty("cassandra.config", "cassandra.yaml")
  System.setProperty("cassandra.storagedir", Files.createTempDirectory("cassandra").toString)
  System.setProperty("cassandra-foreground", "true")

  val cassandraDaemon = new CassandraDaemon()
  cassandraDaemon.activate()

}
