package play.api.libs.weibo
package api

import scala.language.experimental.macros

import play.api.libs.json._

/**
  * ***************************************
  * *********** API ***********************
  * ***************************************
  */

/**
  * weibo api abstraction
  */
trait Api[R] {
  val url: String

  def parse(json: JsValue) : JsResult[R]
}


trait JsonReadsApi[R] extends Api[R] {
  implicit val reads: Reads[R]
  def parse(json: JsValue) = reads.reads(json)
}

abstract class GetApi[R](val url: String)(implicit val reads: Reads[R]) extends JsonReadsApi[R]

abstract class PostApi[R](val url: String)(implicit val reads: Reads[R]) extends JsonReadsApi[R]

object Api {
  private [weibo] implicit class ApiParam[T <: Api[_]](val api: T)
      extends AnyVal {
    def params: Seq[(String, Any)] = macro Macros.paramsImpl[T]
  }

  private object Macros {
    def camelToUnderscores(name: String) = "[A-Z\\d]".r.replaceAllIn(name, {m =>
      "_" + m.group(0).toLowerCase()
    })

    import scala.reflect.macros.Context
    def paramsImpl[T: c.WeakTypeTag](c: Context) = {
      import c.universe._
      val seqApply = Select(reify(Seq).tree, newTermName("apply"))
      val api = Select(c.prefix.tree, newTermName("api"))
      val params = weakTypeOf[T].declarations.collect {
        case m : MethodSymbol if m.isCaseAccessor =>
          val paramName = c.literal(m.name.decoded)
          val paramValue = c.Expr(Select(api, m.name))
          reify(camelToUnderscores(paramName.splice) -> paramValue.splice).tree
      }
      c.Expr[Seq[(String, Any)]](Apply(seqApply, params.toList))
    }
  }
}
