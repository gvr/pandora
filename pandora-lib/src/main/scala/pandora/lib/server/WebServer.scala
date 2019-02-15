package pandora.lib.server

import akka.actor.{ActorRef, ActorRefFactory, ActorSystem}
import akka.event.LoggingAdapter
import akka.http.scaladsl.server.Route

class WebServer private[WebServer] (name: String, ref: ActorRef, log: LoggingAdapter) {

  def stop(): Unit = {
    log.info("Stopping server '{}'...", name)
    ref ! WebServerActor.StopServer
  }

}

private[server] object WebServer {

  def apply(name: String, host: String, port: Int)(implicit system: ActorSystem): Factory =
    new Factory(name, host, port, system)

  class Factory private[WebServer] (name: String, host: String, port: Int, system: ActorSystem) {

    def forRoute(route: Route): WebServer = {
      val ref =
        system.actorOf(WebServerActor.props(name, host, port, route), name)
      new WebServer(name, ref, system.log)
    }

    def forActorRoute(route: (ActorRefFactory, LoggingAdapter) => Route): WebServer =
      forRoute(route(system, system.log))

  }

}
