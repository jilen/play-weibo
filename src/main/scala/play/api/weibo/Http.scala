package play.api.weibo

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

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
 * Http constants defines
 */
object Http {
  sealed trait Method
  case object GET extends Method
  case object POST extends Method
}
