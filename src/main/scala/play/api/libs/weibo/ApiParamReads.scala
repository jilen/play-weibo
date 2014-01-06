package play.api.libs.weibo

import scala.language.experimental.macros
import scala.reflect.macros.Context
import api.Api
object ApiParamReads {

  implicit def caseClassToParamReads[T]: ParamReads[T] = macro Macros.readParamsImpl[T]

  def readParam[A <: Api[_] : ParamReads] (api: A) = {
    implicitly[ParamReads[A]].read(api)
  }
}
