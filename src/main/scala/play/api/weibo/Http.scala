package play.api.weibo

import scala.concurrent.ExecutionContext
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

  val context: ExecutionContext
}

/**
 * Spray implementation
 */
class SprayHttp(val config: SprayHttpConfig) extends Http {
  import config._
  import system.dispatcher

  val context = system.dispatcher

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

  def post(url: String, params: Map[String, Any]): Future[String] =  {
    pipe(buildPost(url, params))
  }

  private def buildPost(url: String, params: Map[String, Any]) = {
    val isMultiPart = params.exists {
      case (_, value: java.io.File)  => true
      case _ => false
    }
    if(isMultiPart) {
      val parts = params.map {
        case (name, value: java.io.File) =>
          BodyPart(value, name)
        case (name, value) =>
          BodyPart(HttpEntity(value.toString), name)
      }.toSeq
      Post(url, MultipartFormData(parts))
    } else {
      val formDatas = params.map {
        case (name, value) =>
          name -> value.toString
      }.toSeq
      Post(url, FormData(formDatas))
    }
  }
}

trait SprayHttpConfig {
  implicit val system : akka.actor.ActorSystem
  val gzipEnable: Boolean
}

/**
 * Http constants defines
 */
object Http {
  sealed trait Method
  case object GET extends Method
  case object POST extends Method
}
