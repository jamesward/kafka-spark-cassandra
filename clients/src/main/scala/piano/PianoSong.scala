package piano

import play.api.libs.json.Json

case class PianoSong(song_id: String, key_codes: Seq[Int])

object PianoSong {
  implicit val writes = Json.writes[PianoSong]
}
