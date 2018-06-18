package org.sasen.kafkaconsumer

import org.sasen.kafkaconsumer.util.ConfigParser

import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

/**
  *
  * @param env
  * @param topic
  */
case class CommandLineArgs(
                            env: String = "dev",
                            topic: String = "")

/**
  * Consumer
  */
object KafkaConsumer extends App {
  val appVersion = "0.1"

  println("Running KafkaConsumer...")

  val parser = new scopt.OptionParser[CommandLineArgs]("KafkaConsumer") {
    head("KafkaConsumer", KafkaConsumer.appVersion)

    opt[String]('e', "env")
      .action( (x, c) => c.copy(env = x) )
      .text("Environment name default to 'dev'")

    opt[String]('t', "topic")
      .required().valueName("<Kafka Topic>")
      .action( (x, c) => c.copy(topic = x) )
      .text("Kafka topic name is required!")
  }

  parser.parse(args, CommandLineArgs()).map { params =>
    val env: String = params.env.toLowerCase
    val kafkaTopic: String = params.topic

    println(s"Environment: $env\nTopic: $kafkaTopic")

    val appProps = ConfigParser.parseProperties(env).
      getOrElse(throw new RuntimeException("Error: Config file must be specified!"))

    val brokers = appProps.bootstrapServers
    val groupId = appProps.groupId

    // TODO - Calls the consumer
    val props = createConsumerConfig(brokers, groupId)

  }.getOrElse {
    println("Bad arguments")
    sys.exit(1)
  }

  def createConsumerConfig(brokers: String, groupId: String): Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }
}
