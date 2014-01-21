package play.api.weibo

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
import play.api.libs.concurrent.Execution.Implicits._
/**
 * weibo api abstraction
 */
trait Api[R] {
  def execute(): Future[Either[WeiboApiError, R]]
}

trait ApiReader[T <: Api[_]] {
  def read[T](api: T): Map[String, Any]
}

trait ApiParser[R] {
  def parse(body: String):  Either[WeiboApiError, R]
}

trait Protocol[A <: Api[R], R] extends ApiReader[A] with ApiParser[R]  {
}

trait GenericHttpApi[R, T <: Api[R]] extends Api[R] with Http {
  import Http._

  def protocol: Protocol[T, R]
  def method: Method

  def execute() = {
    val params = protocol.read(this)
    method match {
      case GET => get(params).map(protocol.parse)
      case POST => post(params).map(protocol.parse)
    }
  }
}

trait HttpApi[R, T <: Api[R]]
    extends GenericHttpApi[R, T] {
  implicit val protocol: Protocol[T, R]
}


abstract class HttGetApi[R, A <: Api[R]](implicit protocol: Protocol) extends HttpApi[R, A] { self: A =>
  val method = Http.GET
}

trait HttpPostApi[R, A <: Api[R]](implicit protocol: Protocol) extends HttpApi[R, A] { self: A =>
  val method = Http.POST
}





case class WeiboApiError(
  error: String,
  errorCode: Int,
  request: String) extends Exception {

  override def getMessage() = s"Weibo api error: ${error}, code: ${errorCode}: request: ${request}"
}
