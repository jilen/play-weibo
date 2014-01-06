package play.api.libs.weibo
package api

import play.api.libs.json._
import play.api.Logger

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
  val params: Map[String, Any]
  def parse(json: JsValue) : JsResult[R]

}


trait JsonReadsApi[A,R] extends Api[R] {

  implicit val reads: Reads[R]

  implicit val paramsReads: ParamReads[A]

  lazy val params = paramsReads.read(this.asInstanceOf[A])

  def parse(json: JsValue) = {
    println("::::::::::::::json" + json)
    if(Logger.isDebugEnabled) {
      Logger.debug("[WeiboClient] parsing json " + json.toString)
    }
    reads.reads(json)
  }

}

abstract class GetApi[A, R] (val url: String)
  (implicit val reads: Reads[R], val paramsReads: ParamReads[A])
    extends JsonReadsApi[A, R]

abstract class PostApi[A, R](val url: String)
  (implicit val reads: Reads[R], val paramsReads: ParamReads[A])
    extends JsonReadsApi[A, R]
