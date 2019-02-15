package pandora.lib.server

import java.io.InputStream
import java.net.InetSocketAddress
import java.security.{KeyStore, SecureRandom}

import akka.actor.{Actor, ActorLogging, Props, Status}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.{ConnectionContext, Http, HttpsConnectionContext}
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import javax.net.ssl._

import scala.concurrent.{ExecutionContext, Future}

private[server] class WebServerActor private[server] (name: String,
                                                      host: String,
                                                      port: Int,
                                                      route: Route,
                                                      connectionContext: ConnectionContext)
    extends Actor
    with ActorLogging {

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContext = context.dispatcher

  private def bindAndHandle: Future[Http.ServerBinding] =
    Http(context.system).bindAndHandle(route, host, port, connectionContext)

  private var bindingFuture: Option[Future[Http.ServerBinding]] = None

  override def preStart(): Unit = {
    super.preStart()
    log.info("Server '{}' starting on {}:{}", name, host, port)
    val future = bindAndHandle.pipeTo(self)
    bindingFuture = Some(future)
  }

  override def receive: Receive = {
    case Http.ServerBinding(address) => handleServerBinding(address)
    case Status.Failure(cause)       => handleBindFailure(cause)
  }

  private def serving: Receive = {
    case WebServerActor.StopServer => stopServer()
    case msg =>
      log.warning("Server '{}' received unexpected message: {}", name, msg)
  }

  private def handleServerBinding(address: InetSocketAddress): Unit = {
    log.info("Server '{}' listening on {}", name, address)
    context.become(serving)
  }

  private def handleBindFailure(cause: Throwable): Unit = {
    log.error(cause, "Server '{}' failed: can't bind to {}:{}", name, host, port)
    context.stop(self)
  }

  private def stopServer(): Unit = {
    bindingFuture.map(_.flatMap(_.unbind()))
    log.info("Server stopped '{}' listening on {}:{}", name, host, port)
  }

  override def postStop(): Unit = {
    stopServer()
    super.postStop()
  }

}

private[server] object WebServerActor {

  def props(name: String, host: String, port: Int, route: Route): Props = {
    val context = ConnectionContext.noEncryption()
    Props(new WebServerActor(name: String, host, port, route, context))
  }

  def props(name: String,
            host: String,
            port: Int,
            route: Route,
            keyStorePassword: String,
            keyStoreFileName: String,
            keyStoreType: String): Props = {
    val context = connectionContext(keyStorePassword, keyStoreFileName, keyStoreType)
    Props(new WebServerActor(name: String, host, port, route, context))
  }

  case object StopServer

  private val webSecurityProtocol = "TLS"

  private def connectionContext(keyStorePassword: String,
                                keyStoreFileName: String,
                                keyStoreType: String): HttpsConnectionContext = {
    val password: Array[Char] = keyStorePassword.toArray

    val keyStore: KeyStore = {
      val store: KeyStore = KeyStore.getInstance(keyStoreType)
      val contents: InputStream = getClass.getClassLoader.getResourceAsStream(keyStoreFileName)
      store.load(contents, password)
      store
    }

    val keyManagers: Array[KeyManager] = {
      val factory: KeyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
      factory.init(keyStore, password)
      factory.getKeyManagers
    }

    val trustManagers: Array[TrustManager] = {
      val factory: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
      factory.init(keyStore)
      factory.getTrustManagers
    }

    val sslContext: SSLContext = {
      val context = SSLContext.getInstance(webSecurityProtocol)
      context.init(keyManagers, trustManagers, new SecureRandom())
      context
    }

    ConnectionContext.https(sslContext)
  }

}
