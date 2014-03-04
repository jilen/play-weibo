package play.api.weibo

import org.scalatest._
import Matchers._

class StatusesTimelineBatchSpec extends ApiSpec {

  behavior of "statuses show api"

  it should "read statuses" in {
    val api = StatusesTimelineBatch(
      accessToken = testAdvancedToken,
      uids = Some("1642909335"))
    val resEither = awaitApi(api)
    val Right(res) = resEither
    res.statuses.size should be > 0
  }
}
