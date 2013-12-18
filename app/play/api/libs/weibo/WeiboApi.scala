package play.api.libs.weibo

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

  def parse(json: JsValue) : R
}


trait JsonReadsApi[R] extends Api[R] {
  implicit val reads: Reads[R]
  def parse(json: JsValue) = reads.reads(json) match {
    case JsSuccess(t, _) => t
    case JsError(errors) => throw new Exception(errors.toString)
  }
}

abstract class GetApi[R](val url: String)(implicit val reads: Reads[R]) extends JsonReadsApi[R]

abstract class PostApi[R](val url: String)(implicit val reads: Reads[R]) extends JsonReadsApi[R]

object Api {
  private [weibo] implicit class ApiParam[R, T <: Api[R]](val api: T)
      extends AnyVal {
    def params: Map[String, Any] = macro Macros.paramsImpl[T]
  }

  private object Macros {
    import scala.reflect.macros.Context
    def paramsImpl[T: c.WeakTypeTag](c: Context) = {
      import c.universe._
      val mapApply = Select(reify(Map).tree, newTermName("apply"))
      val api = Select(c.prefix.tree, newTermName("api"))
      val params = weakTypeOf[T].declarations.collect {
        case m : MethodSymbol if m.isCaseAccessor =>
          val paramName = c.literal(m.name.decoded)
          val paramValue = c.Expr(Select(api, m.name))
          reify(paramName.splice -> paramValue.splice).tree
      }
      c.Expr[Map[String, Any]](Apply(mapApply, params.toList))
    }
  }
}
