package services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import models.PianoSong;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CassandraHelper {

    public static String HOST = "127.0.0.1";
    private Cluster cluster;
    private Session session;

    private static CassandraHelper ourInstance = new CassandraHelper();

    public static CassandraHelper getInstance() {
        return ourInstance;
    }

    private CassandraHelper() {
        this.cluster = Cluster.builder().addContactPoint("127.0.0.1").build();;
        this.session = cluster.connect();
    }

    public List<String> getAllPianoSongs() {
        String songQuery = "SELECT * FROM demo.song";
        System.out.println("getting all songs with query: " + songQuery);
        ResultSet resultSet = session.execute(songQuery);
        List<String> songList = resultSet
                .all()
                .stream()
                .map(row -> row.getString(0))
                .collect(Collectors.toList());

        return songList;
    }

    public Optional<PianoSong> getPianoSong(String querySongId) {

        String songQuery = String.format("SELECT * FROM demo.song where song_id = '%s';", querySongId);
        System.out.println("executing songQuery = " + songQuery);
        ResultSet resultSet = session.execute(songQuery);
        if (resultSet.getAvailableWithoutFetching() > 0) {
            Row firstRow = resultSet.one();
            String songId = firstRow.getString(0);
            List<Integer> keyCodes = firstRow.getList(1, Integer.class);
            return Optional.of(new PianoSong(songId, keyCodes));
        }
        else {
            return Optional.empty();
        }

    }

    public void createPianoKeyspace() {
        session.execute("CREATE KEYSPACE IF NOT EXISTS demo WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};");
        session.execute("CREATE TABLE IF NOT EXISTS demo.song (song_id text,  key_codes list<int>, PRIMARY KEY(song_id));");
        System.out.println("created table for piano data pipeline");
        close();
    }

    public void close() {
        this.cluster.close();
    }

    public Session getSession() {
        return session;
    }
}
