package play.api.weibo

import org.scalatest._
import Matchers._

class StatusesUpdateSpec extends ApiSpec {

   "statuses update api" should "read update" in {
    val api = StatusesUpdate(
      accessToken = testToken,
      status = Generator.randomString(10))
    val resEither = awaitApi(api)
    val Right(res) = resEither
    res.id should be > 0L
  }
}
