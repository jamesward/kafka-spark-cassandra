package piano

object CassandraSetup extends App {

  CassandraHelper.withSession(true) { session =>
    session.execute("CREATE KEYSPACE IF NOT EXISTS demo WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};")

    session.execute("CREATE TABLE IF NOT EXISTS demo.song (song_id text,  key_codes list<int>, PRIMARY KEY(song_id));")

    println("created table for piano data pipeline")
  }

}
