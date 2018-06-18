name := "kafka-consumer"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.2",
  "com.github.scopt" %% "scopt" % "3.7.0",
  "org.apache.kafka" % "kafka-clients" % "1.0.1"
)
