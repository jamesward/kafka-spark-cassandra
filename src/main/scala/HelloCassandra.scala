import java.util.UUID

import com.datastax.driver.core.Cluster

object HelloCassandra extends App {

  val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
  val session = cluster.connect()

  session.execute("CREATE KEYSPACE IF NOT EXISTS demo WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};")

  session.execute("CREATE TABLE IF NOT EXISTS demo.foo (id uuid PRIMARY KEY, name text);")

  session.execute(s"INSERT INTO demo.foo (id, name) VALUES (${UUID.randomUUID().toString}, 'bar');")

  val results = session.execute("SELECT * FROM demo.foo;")

  println(results.all())

  cluster.close()

}
