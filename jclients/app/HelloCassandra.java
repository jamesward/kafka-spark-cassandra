import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.UUID;

public class HelloCassandra {

    public static void main(String[] args) {
        System.out.println("Running Hello Cassandra");


        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        Session session = cluster.connect();

        session.execute("CREATE KEYSPACE IF NOT EXISTS demo WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};");

        session.execute("CREATE TABLE IF NOT EXISTS demo.foo (id uuid PRIMARY KEY, name text);");

        session.execute("CREATE TABLE IF NOT EXISTS demo.rand_ints (job_id int, count int, rand_int int, PRIMARY KEY(job_id, count) );");

        String insertCql = String.format("INSERT INTO demo.foo (id, name) VALUES (%s, 'bar')", UUID.randomUUID().toString());
        System.out.println("insertCql = " + insertCql);
        session.execute(insertCql);

        ResultSet resultSet = session.execute("SELECT * FROM demo.foo;");

        System.out.println(resultSet.all());

        cluster.close();

    }
}
