package piano

import java.util

import com.datastax.driver.core.{Cluster, ResultSet, Row, Session}

import scala.collection.JavaConversions._

object CassandraHelper {

  val host = "127.0.0.1"
  lazy val cassandraConnection = new CassandraHelper

  def withSession[T](f: Session => T): T = {
    withSession(false)(f)
  }

  def withSession[T](autoClose: Boolean)(f: Session => T): T = {
    val result = f(cassandraConnection.session)
    if (autoClose) cassandraConnection.close()
    result
  }

  def closeConnection() = {
    cassandraConnection.close()
  }

  def getPianoSong(querySongId: String) = {
    val songQuery: String = s"SELECT * FROM demo.song where song_id = '$querySongId';"
    println(songQuery)
    val execute: ResultSet = withSession(_.execute(songQuery))
    if (execute.getAvailableWithoutFetching > 0) {
      val firstRow: Row = execute.one()
      val songId: String = firstRow.getString(0)
      val keyCodes = firstRow.getList(1, classOf[Integer]).toSeq.map(_.toInt)

      Some(PianoSong(songId, keyCodes))
    }
    else None
  }

  def getAllPianoSongs() = {
    val songQuery: String = s"SELECT song_id FROM demo.song;"
    val execute: ResultSet = withSession(_.execute(songQuery))
    //execute.all loads the entire song list into memory.  For a production application an iterator should be used instead//
    val songRows = collectionAsScalaIterable(execute.all())
    songRows.map(_.getString(0))
  }
}

class CassandraHelper {
  val cluster = Cluster.builder().addContactPoint(CassandraHelper.host).build()
  val session = cluster.connect()
  def close() = {
    if (!cluster.isClosed) cluster.close()
  }
}
