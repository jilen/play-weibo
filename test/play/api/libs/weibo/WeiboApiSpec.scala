package play.api.libs.weibo.api

import org.specs2.mutable.Specification
import play.api.libs.json._
import java.util.Date

class WeiboApiSpec extends Specification {
  import Api.ApiParam
  case class FakeApi(fooBar: String) extends Api[String]{
    val url = "foo_url"
    def parse(json: JsValue) = ???
  }
  "ApiParam" should {
      "expand case fields to map" in {
      val foo = new FakeApi("foo")
      foo.params must be_==(Seq("foo_bar" -> "foo"))
    }
  }

  case class FakeResult(date: Date)
  implicit val FakeReads = Json.reads[FakeResult]
  case class FakeApi1(implicit reads: Reads[FakeResult]) extends JsonReadsApi[FakeResult]{
    val url = "fake_url1"
  }

  "JsonReadsApi" should {
    "reads json to object" in {
      val api = new FakeApi1
      val res = api.parse(JsObject("date" -> JsString("Fri Dec 20 00:47:19 +0800 2013") :: Nil))
      val JsSuccess(fakeRes, _) = res
      println("parsed res is " + fakeRes)
      fakeRes.date.getTime must be_>(0L)
    }
  }
}
