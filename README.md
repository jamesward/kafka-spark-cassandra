Kafka Spark Cassandra
---------------------

A self-contained sample app that combines Kafka, Spark, and Cassandra.

Start Kafka:

    ./sbt startKafka

Start Cassandra:

    ./sbt startCassandra

Run `HelloKafka`:

    ./sbt helloKafka

Run `HelloCassandra`:

    ./sbt helloCassandra
    
Run `HelloSpark`:

    ./sbt helloSpark
    
Run `HelloSparkStreaming` (requires `HelloSpark` to be running):

    ./sbt helloSparkStreaming

Checkout the Spark UI: [http://localhost:4040](http://localhost:4040)

Run `HelloPlay`:

    ./sbt helloPlay

Check out the app: [http://localhost:8080](http://localhost:8080)
