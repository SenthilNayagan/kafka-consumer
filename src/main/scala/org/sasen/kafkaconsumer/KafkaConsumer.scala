package org.sasen.kafkaconsumer

import org.sasen.kafkaconsumer.util.ConfigParser

case class CommandLineArgs(
                            env: String = "dev",
                            hiveTable: String = "",
                            topic: String = "")

object KafkaConsumer extends App {
  val appVersion = "0.1"

  println("Running KafkaConsumer...")

  val parser = new scopt.OptionParser[CommandLineArgs]("KafkaConsumer") {
    head("KafkaConsumer", KafkaConsumer.appVersion)

    opt[String]('e', "env")
      .action( (x, c) => c.copy(env = x) )
      .text("Environment name default to 'dev'")

    //    opt[String]('h', "hiveTable")
    //      .required().valueName("<Hive Table>")
    //      .action( (x, c) => c.copy(hiveTable = x) )
    //      .text("Hive table name is required!")

    opt[String]('t', "topic")
      .required().valueName("<Kafka Topic>")
      .action( (x, c) => c.copy(topic = x) )
      .text("Kafka topic name is required!")
  }

  parser.parse(args, CommandLineArgs()).map { params =>
    val env: String = params.env.toLowerCase
    //    val hiveTable: String = params.hiveTable
    val kafkaTopic: String = params.topic

    println(s"Environment: $env\nTopic: $kafkaTopic")

    val appProps = ConfigParser.parseProperties(env).
      getOrElse(throw new RuntimeException("Error: Config file must be specified!"))

    println(appProps.bootstrapServers)

    // TODO - Calls the consumer

  }.getOrElse {
    println("Bad arguments")
    sys.exit(1)
  }
}
