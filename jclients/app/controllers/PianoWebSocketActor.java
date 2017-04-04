package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import services.KafkaHelper;

import java.util.Properties;


public class PianoWebSocketActor extends UntypedActor {

    public static Props props(ActorRef out) {
        return Props.create(PianoWebSocketActor.class, out);
    }

    private final ActorRef out;

    Properties kafkaStreamsConfig = KafkaHelper.getPianoKafkaStreamsConfig();
    Producer<String, Integer> producer = new KafkaProducer<>(kafkaStreamsConfig);

    public PianoWebSocketActor(ActorRef out) {
        this.out = out;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof JsonNode) {
            JsonNode parsedJson = (JsonNode) message;
            String songId = parsedJson.get("songId").asText();
            Integer keyCode = parsedJson.findValue("keyCode").asInt(Integer.MAX_VALUE);
            ProducerRecord<String, Integer> producerRecord = new ProducerRecord<>(KafkaHelper.TOPIC, songId, keyCode);
            producer.send(producerRecord);
        }
    }

    @Override
    public void postStop() throws Exception {
        producer.close();
        super.postStop();
    }
}
