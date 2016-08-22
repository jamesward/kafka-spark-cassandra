Kafka Spark Cassandra
---------------------

A self-contained sample app that combines Kafka, Spark, and Cassandra.

Start Kafka:

    ./sbt startKafka

Start Cassandra:

    ./sbt startCassandra

Run `HelloKafka`:

    ./sbt "runMain HelloKafka"

Run `HelloCassandra`:

    ./sbt "runMain HelloCassandra"
    
Run `HelloSpark`:

    ./sbt "runMain HelloSpark"
    
Run `HelloSparkStreaming`:

    ./sbt "runMain HelloSparkStreaming"    

Run `HelloHttp4s`:

    ./sbt "runMain HelloHttp4s"
