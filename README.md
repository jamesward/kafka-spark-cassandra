Kafka Spark Cassandra
---------------------

A self-contained sample app that combines Kafka, Spark, and Cassandra.

Start Kafka:

    ./sbt kafkaServer/run

Start Cassandra:

    ./sbt cassandraServer/run

Run `HelloKafka`:

    ./sbt "clients/runMain HelloKafka"

Run `HelloCassandra`:

    ./sbt "clients/runMain HelloCassandra"
    
Run `HelloSpark`:

    ./sbt "clients/runMain HelloSpark"
    
Run `HelloSparkStreaming` (requires `HelloSpark` to be running):

    ./sbt "clients/runMain HelloSparkStreaming"

Checkout the Spark UI: [http://localhost:4040](http://localhost:4040)

Run `HelloPlay`:

    ./sbt "clients/runMain HelloPlay"

Check out the app: [http://localhost:8080](http://localhost:8080)
