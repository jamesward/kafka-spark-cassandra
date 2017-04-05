package piano;

import com.datastax.spark.connector.CollectionColumnName;
import com.datastax.spark.connector.ColumnName;
import com.datastax.spark.connector.ColumnRef;
import com.datastax.spark.connector.SomeColumns;
import com.datastax.spark.connector.util.JavaApiHelper;
import models.PianoSong;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Seconds;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Option;
import services.CassandraHelper;
import services.KafkaHelper;

import java.util.*;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;
import static com.datastax.spark.connector.japi.CassandraStreamingJavaUtil.javaFunctions;

public class SparkStreaming {

    public static void main(String[] args) throws Exception {

        SparkConf sparkConf = new SparkConf()
                .setMaster("local[1]")
                .setAppName("piano-spark-streaming")
                .set("spark.cassandra.connection.host", CassandraHelper.HOST)
                .set("spark.sql.warehouse.dir", "spark-warehouse");

        // Create the context with 1 seconds batch size
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Seconds.apply(1));

        Random random = new Random();

        final Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", KafkaHelper.BOOTSTRAP_SERVICE);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", IntegerDeserializer.class);
        kafkaParams.put("auto.offset.reset", "earliest");
        kafkaParams.put("group.id", "java-test-consumer-" + random.nextInt() +
                "-" + System.currentTimeMillis());

        // todo: save kafka stream to cassandra

        jssc.start();
        jssc.awaitTermination();
    }

    static PianoSong toPianoSong(ConsumerRecord<String, Integer> consumerRecord) {
        List<Integer> records = Arrays.asList(consumerRecord.value());
        PianoSong pianoSong = new PianoSong(consumerRecord.key(), records);
        System.out.println("pianoSong = " + pianoSong);
        return pianoSong;
    }

}
