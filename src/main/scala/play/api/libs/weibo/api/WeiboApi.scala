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

abstract class GetApi[R](val url: String)(implicit val reads: Reads[R])
    extends JsonReadsApi[R]

abstract class PostApi[R](val url: String)(implicit val reads: Reads[R])
    extends JsonReadsApi[R]
