package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import java.io.Serializable;
import java.util.List;

public class PianoSong implements Serializable {

    private String song_id;
    private List<Integer> key_codes;

    public PianoSong() {
    }

    public PianoSong(String song_id, List<Integer> key_codes) {
        this.song_id = song_id;
        this.key_codes = key_codes;
    }

    public JsonNode pianoSongToJson() {
        return Json.toJson(this);
    }


    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public List<Integer> getKey_codes() {
        return key_codes;
    }

    public void setKey_codes(List<Integer> key_codes) {
        this.key_codes = key_codes;
    }

    @Override
    public String toString() {
        return "PianoSong{" +
                "song_id='" + song_id + '\'' +
                ", key_codes=" + key_codes +
                '}';
    }
}
