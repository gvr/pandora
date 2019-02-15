import sbt._

object Dependencies {

  private val versions = new {
    val akkaActors: String = "2.5.21"
    val akkaHttp: String = "10.1.7"
    val akkaStreams: String = akkaActors
    val logback: String = "1.2.3"
    val micrometer: String = "1.1.3"
    val scalatest: String = "3.0.5"
  }

  private val akkaActors: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor" % versions.akkaActors,
    "com.typesafe.akka" %% "akka-testkit" % "2.5.20" % Test
  )

  private val akkaStreams: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-stream" % versions.akkaStreams,
    "com.typesafe.akka" %% "akka-stream-testkit" % versions.akkaStreams % Test
  )

  private val akkaHttp: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-http" % versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-spray-json" % versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-testkit" % versions.akkaHttp % Test
  )

  private val akka: Seq[ModuleID] = akkaActors ++ akkaStreams ++ akkaHttp

  private val logging: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-slf4j" % versions.akkaActors,
    "ch.qos.logback" % "logback-classic" % versions.logback
  )

  private val monitor: Seq[ModuleID] = Seq(
    "io.micrometer" % "micrometer-registry-prometheus" % versions.micrometer
  )

  private val test: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % versions.scalatest % Test
  )

  val core: Seq[ModuleID] = test

  val lib: Seq[ModuleID] = akka ++ logging ++ monitor ++ test

  val sandbox: Seq[ModuleID] = akka ++ logging ++ monitor ++ test

}
