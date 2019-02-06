package pandora.sandbox

import java.util.concurrent.atomic.AtomicInteger

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import io.micrometer.core.instrument.Counter
import io.micrometer.prometheus.{PrometheusConfig, PrometheusMeterRegistry}
import pandora.lib.server.ServerSystem
import spray.json._

object HelloAgainServer extends App with SprayJsonSupport {

  private val counter: AtomicInteger = new AtomicInteger(0)

  object Response extends DefaultJsonProtocol with SprayJsonSupport {

    case class Hello(name: String, msg: String, n: Int)

    implicit val helloJsonFoprmat: RootJsonWriter[Hello] = jsonFormat3(Hello)

    lazy val version: JsValue = VersionInfo.asMap.toJson

  }

  object Metrics {

    private val registry: PrometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    registry.config().commonTags("host", "localhost")

    private def counter(endpoint: String): Counter = registry.counter("pandora.sandbox.requests", "endpoint", endpoint)

    val helloCounter: Counter = counter("hello")
    val versionCounter: Counter = counter("version")
    val metricsCounter: Counter = counter("metrics")

    def scrape(): String = registry.scrape()
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
      complete(response(name).toJson)
    } ~
      path("version") {
        Metrics.versionCounter.increment()
        complete(Response.version)
      } ~
      path("metrics") {
        Metrics.metricsCounter.increment()
        complete(Metrics.scrape())
      }
  }

  private val system: ServerSystem = ServerSystem()
  system.http("hello", "0.0.0.0", 8080).forRoute(route)

}
