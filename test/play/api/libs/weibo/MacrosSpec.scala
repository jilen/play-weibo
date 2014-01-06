package play.api.libs.weibo

import scala.language.experimental.macros
import org.specs2.mutable.Specification
import api._
import play.api.libs.json._

case class Bar(fooBar: String)
case class Foo(fooBar: String)

class MacrosSpec extends Specification {
  val fooReads = paramReads[Foo]
  "Macros" should {
    "read params" in {
      val foo = Foo("baz")
      fooReads.read(foo) must be_==(Map("foo_bar" -> "baz"))
    }

    "read json" in {
      val reads = readsUnderscore[Bar]
      val json = """{"foo_bar": "baz"}"""
      val res = reads.reads(Json.parse(json))
      println("::::" + res)
      true
    }
  }

}
