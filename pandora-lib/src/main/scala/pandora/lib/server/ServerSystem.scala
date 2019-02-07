package pandora.lib.server

import akka.actor.ActorSystem
import akka.event.LoggingAdapter

import scala.concurrent.duration._
import scala.concurrent.{Await, TimeoutException}
import scala.language.postfixOps

class ServerSystem(systemName: String) {

  private implicit val system: ActorSystem = ActorSystem(systemName)

  private lazy val log: LoggingAdapter = system.log

  def http(name: String, host: String, port: Int): WebServer.Factory = WebServer(name, host, port)

  sys.addShutdownHook {
    log.info("Exiting server '{}'...", systemName)
    try {
      Await.result(system.terminate(), 2 seconds)
      log.info("Server '{}' exited normally", systemName)
    }
    catch {
      case _: TimeoutException => log.warning("Server '{}' exited without timely shutdown", systemName)
      case t: Throwable => log.warning("Server '{}' exited with unexpected exception: {}", systemName, t.getMessage)
    }
  }

}

object ServerSystem {

  private val defaultSystemName: String = "server-system"

  def apply(systemName: String = defaultSystemName): ServerSystem = new ServerSystem(systemName)

}
