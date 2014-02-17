package play.api.weibo

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

class StatusesShowBatchSpec extends ApiSpec {

  "'statuses show batch' api" should {
    "read statuses" in {
      val api = StatusesShowBatch(
        accessToken = testAdvancedToken,
        ids = "3677163356078857")
      val res = awaitApi(api)
      res.statuses must have size (1)
    }
  }
}
