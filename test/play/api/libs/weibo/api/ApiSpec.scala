package play.api.libs.weibo.api

import org.specs2.mutable.Specification
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.weibo.WeiboClient

trait ApiSpec extends Specification {
  def await[R](api: Api[R]) = Await.result(
    WeiboClient.validate(api),
    Duration.Inf)
}
