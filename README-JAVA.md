Kafka Spark Cassandra Piano
-----------------------------

A self-contained sample app that combines Kafka, Spark, and Cassandra.  

The final app is a web piano that records by streaming the data from the web browser -> play -> kafka -> spark streaming -> cassandra

Start Kafka:

    ./sbt startKafka

Start Cassandra:

    ./sbt startCassandra

Run `HelloKafka`:

    ./sbt jHelloKafka

Run `HelloCassandra`:

    ./sbt jHelloCassandra

Run `HelloPlay`:

    ./sbt jHelloPlay

Check out the app: [http://localhost:9000](http://localhost:9000)

# Setup and running the piano app

Run `setupPiano` to set up the Cassandra Schema

    ./sbt jSetupPiano

Run `pianoSparkStreaming` to start Spark Streaming

    ./sbt jPianoSparkStreaming

Then checkout the Spark UI: [http://localhost:4040](http://localhost:4040)

Check out the app: [http://localhost:9000/piano](http://localhost:9000/piano)

# Overview of main directories and files

### Play Project Directories

**jclients/app**   ->   The root of the Play Java project.  It contains the controllers and view.
  
**jclients/conf**  ->   The Play Project configuration directory.
  
**jclients/conf/routes** ->  The Play Project routes file that routes requests to controllers and views. 

**jclients/app/piano**  -> The Sample Piano Code

**jclients/app/piano/CassandraSetup** -> This creates the sample Cassandra schema for the Piano app.

**jclients/app/piano/SparkStreaming** -> This is the Spark Streaming example for the Piano app.

**cassandra-server**  ->  This is an app that starts an embedded cassandra server 

**kafka-server**  ->  This is an app that starts an embedded kafka server

# Examples of Spark and Spark Streaming in Java

[Spark Demos contains examples in Java of Spark and Spark Streaming](https://github.com/retroryan/DataStaxSparkDemos)

These samples can be easily run with OSS Spark and Cassandra by changing the setup of the spark context to use the following:
 
 ```
        SparkConf sparkConf = new SparkConf()
                .setMaster("local[1]")
                .setAppName("piano-spark-streaming")
                .set("spark.cassandra.connection.host", "localhost")
                .set("spark.sql.warehouse.dir", "spark-warehouse");

```

# Verifying kafka

Text is garrabled because it is being written using an Integer serializer

$KAFKA_HOME/bin/kafka-console-consumer.sh --from-beginning --zookeeper localhost:2181 --topic RandomNumbers
