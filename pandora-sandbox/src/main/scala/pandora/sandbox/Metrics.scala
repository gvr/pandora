package pandora.sandbox

import io.micrometer.core.instrument._
import io.micrometer.prometheus.{PrometheusConfig, PrometheusMeterRegistry}

object Metrics {

  private val registry: PrometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
  registry.config().commonTags("host", "localhost")

  def scrape(): String = registry.scrape()

  // counters
  private def counter(endpoint: String): Counter = registry.counter("pandora.sandbox.requests", "endpoint", endpoint)

  lazy val helloCounter: Counter = counter("hello")

  lazy val versionCounter: Counter = counter("version")

  lazy val metricsCounter: Counter = counter("metrics")

  lazy val waitCounter: Counter = counter("wait")

  // timers
  private def timer(endpoint: String): Timer = Timer
    .builder("pandora.sandbox.responses")
    .tags("endpoint", endpoint)
    .publishPercentileHistogram()
    .register(registry)


  lazy val helloTimer = new BlockTimer("hello")

  lazy val versionTimer = new BlockTimer("version")

  lazy val metricsTimer = new BlockTimer("metrics")

  private lazy val waitBaseTimer = timer("wait")
  lazy val waitTimer: StartedTimer = {
    val startTime = System.currentTimeMillis()
    new StartedTimer(waitBaseTimer, startTime)
  }

  class BlockTimer private[Metrics] (endpoint: String) {

    private lazy val baseTimer = timer(endpoint)

    def measure[T](block: => T): T = {
      val startTime = System.currentTimeMillis()
      val result: T = block
      val duration = System.currentTimeMillis() - startTime
      baseTimer.record(java.time.Duration.ofMillis(duration))
      result
    }

  }

  class StartedTimer(timer: Timer, startTime: Long) {

    def measure(): Long = {
      val duration = System.currentTimeMillis() - startTime
      timer.record(java.time.Duration.ofMillis(duration))
      duration
    }

  }

}
