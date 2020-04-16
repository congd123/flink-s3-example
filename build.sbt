name := "examples-s3"
organization in ThisBuild := "com.examples"
scalaVersion in ThisBuild := "2.12.10"
version in ThisBuild := "test"

test in assembly := {}

libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala" % "1.10.0" % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % "1.10.0" % "provided",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.734" % "provided",
  "org.apache.flink" %% "flink-parquet" % "1.10.0",
  "org.apache.hadoop" % "hadoop-aws" % "2.8.5",
  "org.apache.parquet" % "parquet-avro" % "1.10.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x                             => MergeStrategy.first
}
