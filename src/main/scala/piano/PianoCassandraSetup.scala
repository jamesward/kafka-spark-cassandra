package piano

import java.util.UUID

import com.datastax.driver.core.Cluster

object PianoCassandraSetup extends App {

  val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
  val session = cluster.connect()

  session.execute("CREATE KEYSPACE IF NOT EXISTS demo WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};")

  session.execute("CREATE TABLE IF NOT EXISTS demo.song (client_id int, song_id int,  key_codes list<int>, PRIMARY KEY(client_id, song_id));")

  println("created table for piano data pipeline")
  cluster.close()

}
