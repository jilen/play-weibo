package play.api.weibo



/**
 * weibo api abstraction
 */
trait Api[R] {
  def execute : Future[Either[WeiboApiError, R]]
}

trait GenericHttpApi[R] extends Api[R] with Http {
  def params: Map[String, Any]
  def parse(body: String): Either[WeiboApiError, R]
}

case class WeiboApiError(
  error: String,
  errorCode: Int,
  request: String) extends Exception {

  override def getMessage() = s"Weibo api error: ${error}, code: ${errorCode}: request: ${request}"
}
