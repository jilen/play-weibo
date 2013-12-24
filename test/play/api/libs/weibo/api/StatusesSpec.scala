package play.api.libs.weibo.api

import org.specs2.mutable.Specification
import play.api.libs.weibo._

class StatusesSpec extends ApiSpec {
  "'StatusShow' api" should {
    "read status" in {
      val api = StatusesShow(
        accessToken = testToken,
        id = 3481475946781445L
      )
      await(api).id should be_==(api.id)
    }
  }
}
