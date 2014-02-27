package play.api.weibo

import org.scalatest._
import Matchers._

class StatusesShowBatchSpec extends ApiSpec {

  behavior of "statuses show api"

  it should "read statuses" in {
    val api = StatusesShowBatch(
      accessToken = testAdvancedToken,
      ids = "3677163356078857")
    val resEither = awaitApi(api)
    val Right(res) = resEither
    res.statuses should have size 1
  }
}
