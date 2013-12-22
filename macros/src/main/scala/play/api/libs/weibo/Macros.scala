package play.api.libs.weibo
import scala.reflect.macros.Context
import scala.language.experimental.macros

private[weibo] object Macros {

  private def camelToUnderscores(name: String) = {
    "[A-Z\\d]".r.replaceAllIn(
      name,
      {m => "_" + m.group(0).toLowerCase()
      })
  }

  def readParamsImpl[T: c.WeakTypeTag](c: Context)(api: c.Expr[T]) = {
    import c.universe._
    val mapApply = Select(reify(Map).tree, newTermName("apply"))
    val params = weakTypeOf[T].declarations.collect {
      case m : MethodSymbol if m.isCaseAccessor =>
        val paramName = c.literal(m.name.decoded)
        val paramValue = c.Expr(Select(api.tree, m.name))
        reify(camelToUnderscores(paramName.splice) -> paramValue.splice).tree
    }
    c.Expr[Map[String, Any]](Apply(mapApply, params.toList))
  }

}
