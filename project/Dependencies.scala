import sbt._

object Dependencies {

  private val versions = new {
    val akkaActors = "2.5.20"
    val akkaHttp = "10.1.7"
    val akkaStreams = "2.5.20"
    val logback = "1.2.3"
    val micrometer = "1.1.2"
    val scalatest = "3.0.5"
  }

  val akkaActors: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor" % versions.akkaActors,
    "com.typesafe.akka" %% "akka-testkit" % "2.5.20" % Test
  )
  
  val akkaStreams: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-stream" % versions.akkaStreams,
    "com.typesafe.akka" %% "akka-stream-testkit" % versions.akkaStreams % Test
  )

  val akkaHttp: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-http" % versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-spray-json" % versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-testkit" % versions.akkaHttp % Test

  )
  
  val logging: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-slf4j" %versions.akkaActors,
    "ch.qos.logback" % "logback-classic" % versions.logback
  )
  
  val monitor: Seq[ModuleID] = Seq(
    "io.micrometer" % "micrometer-registry-prometheus" % versions.micrometer
  )
  
  val test: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % versions.scalatest % Test
  )
  
  val akka: Seq[ModuleID] = akkaActors ++ akkaStreams ++ akkaHttp
  
}
