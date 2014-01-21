package play.api.weibo

import scala.concurrent.Future
/**
 * Http transport abstract
 */
trait Http {
  def get(params: Map[String, Any]): Future[String]
  def post(param: Map[String, Any]): Future[String]
}

/**
 * Http constants defines
 */
object Http {
  sealed trait Method
  case object GET extends Method
  case object POST extends Method
}
