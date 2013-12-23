package play.api.libs.weibo.api

import org.specs2.mutable.Specification
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.weibo.WeiboClient
import play.api.libs.json._

trait ApiSpec extends Specification {
  val testToken = "2.00f8pTwBIVrwXEc4f5bb8f2cToXFCB"
  def await[R](api: Api[R]) ={
    val jsResult = Await.result(
      WeiboClient.validate(api),
      Duration.Inf)
    val JsSuccess(res, _) = jsResult
    res
  }
}
