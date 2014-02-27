package play.api.weibo

import org.scalatest._
import Matchers._

class StatusesUploadSpec extends ApiSpec {

  "statuses updload api" should "read update" in {

    val api = StatusesUpload(
      accessToken = testToken,
      status = Generator.randomString(10),
      pic = new java.io.File("src/test/resources/images/test.jpg"))

    val resEither = awaitApi(api)
    val Right(res) = resEither
    res.id should be > 0L
  }
}
