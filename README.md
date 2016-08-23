Kafka Spark Cassandra
---------------------

A self-contained sample app that combines Kafka, Spark, and Cassandra.

Start Kafka:

    ./sbt startKafka

Start Cassandra:

    ./sbt startCassandra

Run `HelloKafka`:

    ./sbt "runMain HelloKafka"
    
    If you have kafka on your local machine you can watch the messages from the command line with:
    
    bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic RandomNumbers --from-beginning

Run `HelloCassandra`:

    ./sbt "runMain HelloCassandra"
    
Run `HelloSpark`:

    ./sbt "runMain HelloSpark"
    
Run `HelloSparkStreaming`:

    ./sbt "runMain HelloSparkStreaming"    

Checkout the Spark UI: [http://localhost:4040](http://localhost:4040)

Run `HelloPlay`:

    ./sbt "runMain HelloPlay"
