package pandora.sandbox

import java.util.concurrent.atomic.AtomicInteger

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import pandora.lib.server.ServerSystem
import spray.json._

import scala.concurrent.{ExecutionContext, Future}

object HelloAgainServer extends App with SprayJsonSupport {

  private val counter: AtomicInteger = new AtomicInteger(0)

  object Response extends DefaultJsonProtocol with SprayJsonSupport {

    case class Hello(name: String, msg: String, n: Int)

    implicit val helloJsonFoprmat: RootJsonWriter[Hello] = jsonFormat3(Hello)

    lazy val version: JsValue = VersionInfo.asMap.toJson

  }

  def response(name: String): Response.Hello = {
    val n = counter.incrementAndGet()
    Response.Hello(name, "Hi there!", n)
  }

  val host: String = "0.0.0.0"

  val port: Int = 8080

  val route = get {
    path("hello" / Segment) { name =>
      Metrics.helloCounter.increment()
      complete(handleHello(name))
    } ~
      path("version") {
        Metrics.versionCounter.increment()
        complete(handleVersion())
      } ~
      path("metrics") {
        Metrics.metricsCounter.increment()
        complete(handleMetrics())
      } ~
      path("wait") {
        Metrics.waitCounter.increment()
        complete(handleWait())
      }
  }

  def handleHello(name: String): JsValue = Metrics.helloTimer.measure {
    response(name).toJson
  }

  def handleVersion(): JsValue = Metrics.versionTimer.measure {
    Response.version
  }

  def handleMetrics(): String = Metrics.metricsTimer.measure {
    Metrics.scrape()
  }

  def handleWait(): Future[String] = {
    implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global
    val timer = Metrics.waitTimer
    Future({
      val t = 2000L
      Thread.sleep(t)
      timer.measure()
      "waiting is over..."
    })
  }

  private val system: ServerSystem = ServerSystem()
  system.http("hello", host, port).forRoute(route)

}
