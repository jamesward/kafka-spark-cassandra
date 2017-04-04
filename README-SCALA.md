Kafka Spark Cassandra
---------------------

A self-contained sample app that combines Kafka, Spark, and Cassandra.

Start Kafka:

    ./sbt startKafka

Start Cassandra:

    ./sbt startCassandra

Run `HelloKafka`:

    ./sbt sHelloKafka

Run `HelloCassandra`:

    ./sbt sHelloCassandra
    
Run `HelloSpark`:

    ./sbt sHelloSpark
    
Run `HelloSparkStreaming` (requires `HelloSpark` to be running):

    ./sbt sHelloSparkStreaming

Checkout the Spark UI: [http://localhost:4040](http://localhost:4040)

Run `HelloPlay`:

    ./sbt sHelloPlay

Check out the app: [http://localhost:9000](http://localhost:9000)
