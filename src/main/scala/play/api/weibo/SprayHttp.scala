package play.api.weibo

import akka.actor.ActorSystem
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import spray.client.pipelining._
import spray.http._
import spray.httpx.encoding.Gzip
import spray.httpx.unmarshalling._
import spray.http.Uri
import spray.http.Uri.Query
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

  val pipe = if(gzipEnabled) pipeGzip else pipePlain

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
  val gzipEnabled: Boolean
}

object SprayHttp {

  def gziped(implicit actorSystem: ActorSystem) = {
    val cfg = new SprayHttpConfig {
      val system = actorSystem
      val gzipEnabled = true
    }
    new SprayHttp(cfg)
  }

  def plain(implicit actorSystem: ActorSystem) = {
    val cfg = new SprayHttpConfig {
      val system = actorSystem
      val gzipEnabled = false
    }
    new SprayHttp(cfg)
  }
}
