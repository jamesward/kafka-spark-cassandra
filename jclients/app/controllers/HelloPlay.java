package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.PianoSong;
import play.libs.Json;
import play.mvc.*;
import services.CassandraHelper;
import views.html.index;

import java.util.List;
import java.util.Optional;

@Singleton
public class HelloPlay extends Controller {

    @Inject
    private CassandraHelper cassandraHelper;

    public Result index() {
        return ok("Hello World");
    }

    public Result piano() {
        return ok(index.render());
    }

    public Result songs() {
        List<String> allPianoSongs = cassandraHelper.getAllPianoSongs();
        JsonNode pianoSongJson = Json.toJson(allPianoSongs);
        return ok(pianoSongJson);
    }

    public Result play(String id) {
        Optional<PianoSong> pianoSong = cassandraHelper.getPianoSong(id);
        return pianoSong.map(pianoSong1 -> ok(Json.toJson(pianoSong1))).orElseGet(Results::notFound);
    }

    /**
     * This is the deprecated way of creating web sockets,
     * but is a much easier API to work with for small examples
     */
    public LegacyWebSocket<JsonNode> pianoSocket() {
        return WebSocket.withActor(PianoWebSocketActor::props);
    }

}
