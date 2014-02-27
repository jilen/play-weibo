package play.api.weibo

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
/**
 * weibo api abstraction
 */
trait Api[R] {
  def execute(implicit http: Http): Future[Either[WeiboApiError, R]]
}

trait GenericHttpApi[R, T <: Api[R]] extends Api[R] {
  import Http._
  def protocol: Protocol[T, R]
  def method: Method
  def url: String

  def execute(implicit http: Http) = {
    val params = protocol.read(this)
    implicit val ctx =  http.context
    method match {
      case GET => http.get(url,params).map(protocol.parse)
      case POST => http.post(url, params).map(protocol.parse)
    }
  }
}

trait HttpApi[R, T <: Api[R]]
    extends GenericHttpApi[R, T] {
  implicit val protocol: Protocol[T, R]
}


abstract class HttpGetApi[ A <: Api[R], R](val url: String)(implicit val protocol: Protocol[A, R])
    extends HttpApi[R, A]{ self: A =>
  val method = Http.GET
}

abstract class HttpPostApi[ A <: Api[R], R](val url: String)(implicit val protocol: Protocol[A, R])
    extends HttpApi[R, A]{ self: A =>
  val method = Http.POST
}

case class WeiboApiError(
  error: String,
  errorCode: Int,
  request: String) extends Exception {

  override def getMessage() = s"Weibo api error: ${error}, code: ${errorCode}: request: ${request}"
}
