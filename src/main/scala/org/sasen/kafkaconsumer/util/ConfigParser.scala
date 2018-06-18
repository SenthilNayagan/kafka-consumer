package org.sasen.kafkaconsumer.util

import com.typesafe.config.{ConfigFactory}


class ConfigParser(val zookeeperQuorum: String,
                   val bootstrapServers: String,
                   val groupId: String) {

  override def toString: String = s"Args: \n\t$zookeeperQuorum \n\t$bootstrapServers"
}

object ConfigParser {
  // Parses .conf file depending on the given environment name
  def parseProperties(envName: String): Option[ConfigParser] = {
    val propFilename = envName + ".conf"
    println("Config filename: " + propFilename)

    try {
      val conf = ConfigFactory.parseResources(propFilename)

      Option(new ConfigParser(
        conf.getString("zookeeper.properties.zk.quorum"),
        conf.getString("kafka.properties.bootstrap.servers"),
        conf.getString("kafka.properties.group.id"))
      )
    }
    catch {
      case ex: Exception => None
    }
  }
}

