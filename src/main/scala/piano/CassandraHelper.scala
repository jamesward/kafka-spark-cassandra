package piano

import com.datastax.driver.core.{Cluster, ResultSet, Row, Session}

import scala.collection.JavaConversions._

object CassandraHelper {

  val host = "127.0.0.1"

  def withSession[T](f: Session => T): T = {
    withSession(false)(f)
  }

  def withSession[T](autoClose: Boolean)(f: Session => T): T = {
    val cluster = Cluster.builder().addContactPoint(host).build()
    val result = f(cluster.connect())
    if (autoClose) cluster.close()
    result
  }

  def getPianoSong(querySongId: String) = {
    val songQuery: String = s"SELECT * FROM demo.song where song_id = '$querySongId';"
    val execute: ResultSet = withSession(_.execute(songQuery))
    val firstRow: Row = execute.one()
    val songId: String = firstRow.getString(0)
    val keyCodes = firstRow.getList(1, classOf[Integer]).toSeq.map(_.toInt)

    PianoSong(songId , keyCodes)
  }
}

