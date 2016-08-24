package piano

import com.datastax.driver.core.{Row, ResultSet, Cluster}
import scala.collection.JavaConversions._

object PianoCassandraHelper extends App {

  lazy val pianoCassandraHelper = new  PianoCassandraHelper
}

class PianoCassandraHelper {
  val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
  val session = cluster.connect()


  def getPianoSong(query_client_id: Int, query_song_id: Int) = {

    val songQuery: String = s"SELECT * FROM demo.song where client_id = $query_client_id and song_id = $query_song_id;"
    val execute: ResultSet = session.execute(songQuery)
    val firstRow: Row = execute.one()
    val client_id: Int = firstRow.getInt(0)
    val song_id: Int = firstRow.getInt(1)
    val key_codes = firstRow.getList(2, classOf[Integer]).toSeq.map(_.asInstanceOf[Int])

    PianoSong(client_id, song_id , key_codes)
  }
}

