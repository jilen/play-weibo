package play.api.weibo

import scala.concurrent.Future
import spray.client.pipelining._
import spray.http._
import spray.httpx.encoding.Gzip
import spray.httpx.unmarshalling._
import spray.http.Uri
import spray.http.Uri.Query

/**
 * Http transport abstract
 */
trait Http {
  /**
   * Perform http get request with query params
   */
  def get(url: String, params: Map[String, Any]): Future[String]
  /**
   * Perform http post request, should implement multi-part params
   */
  def post(url: String, param: Map[String, Any]): Future[String]
}

/**
 * Spray implementation
 */
trait SprayHttp extends Http {

  private val logger = play.api.Logger(classOf[SprayHttp])

  val config: SprayHttpConfig
  import config._
  import system.dispatcher

  val unmarshalWeibo: HttpResponse => String = {
    case HttpResponse(code, body, header, _) =>
      body.as[String].right.get
  }

  val pipePlain: HttpRequest => Future[String] = (
    sendReceive ~> unmarshalWeibo
  )

  val pipeGzip: HttpRequest => Future[String] = (
    addHeader("Accept-Encoding", "gzip")
      ~> sendReceive
      ~> decode(Gzip)
      ~> unmarshalWeibo)

  val pipe = if(gzipEnable) pipeGzip else pipePlain

  def get(url: String, params: Map[String, Any]): Future[String] =  {
    val queryUri = Uri(url).copy(query = Query(params.mapValues(_.toString)))
    pipe(Get(queryUri))
  }

  def post(url: String, param: Map[String, Any]): Future[String] = ???
}

trait SprayHttpConfig {
  implicit val system : akka.actor.ActorSystem
  implicit val gzipEnable: Boolean
}

/**
 * Http constants defines
 */
object Http {
  sealed trait Method
  case object GET extends Method
  case object POST extends Method

  class PlaySprayConfig(val gzipEnable: Boolean) extends SprayHttpConfig {
    import play.api.libs.concurrent.Akka
    import play.api.Play.current
    val system = Akka.system
  }

  implicit object DefaultHttp extends SprayHttp {
    val config = new PlaySprayConfig(false)

  }

  implicit object GzipHttp extends {
    val config = new PlaySprayConfig(true)
  }
}
