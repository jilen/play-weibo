package play.api.libs.weibo

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws._
import play.api.libs.json._
import scala.concurrent._
import scala.concurrent.duration._
import api._
import scala.language.experimental.macros

object WeiboClient {

  private val logger = play.api.Logger(this.getClass)
  private[weibo] val tokenUrl = "https://api.weibo.com/oauth2/access_token"

  private def apiParams[T <: Api[_]](api: T) = macro Macros.readParamsImpl[T]

  def validate[R](api: Api[R]) = {
    val params = apiParams(api)
      (api match {
        case _ : GetApi[_] =>
          get(api.url, params)
        case _ : PostApi[_] =>
          val isMultiPart = params.exists(_._2.isInstanceOf[java.io.File])
          post(api.url, params, isMultiPart)
      }).map{resp => api.parse(resp.json)
      }
  }


  def oauthUrl(appClient: String, redirectUri: String) = {
    "https://api.weibo.com/oauth2/authorize?client_id=" + appClient +
    "&response_type=code&redirect_uri=" + redirectUri
  }

  def getAccessToken(
    appClient: String,
    appSecret: String,
    code: String,
    redirectUri: String) = {
    val future = post(tokenUrl, Map(
      "client_id" -> appClient,
      "client_secret" -> appSecret,
      "grant_type" -> "authorization_code",
      "code" -> code,
      "redirect_uri" -> redirectUri),
      false) map { resp =>
      resp.json.validate[AccessToken]
    }
  }

  /**
    * Perform an  http(s) get request
    */
  def get(url: String, params: Map[String, Any]) = {
    val req = params.foldLeft(WS.url(url)) {
      (req, kv) => req.withQueryString(kv._1 -> kv._2.toString)
    }
    if (logger.isDebugEnabled) {
      logger.debug("weibo get request: " + req)
    }
    req.get()
  }

  /**
    * Perform an http(s) post request
    */
  def post(url: String, params: Map[String, Any], multiPart: Boolean) = {

    def postFormData(): Future[Response] = {
      val normalized = params.map {
        case (k, v) => k -> Seq(v.toString)
      }
      WS.url(url).post(normalized)
    }
    def postMultiPart(): Future[Response] = ???
    if(multiPart)
      postMultiPart()
    else
      postFormData()
  }


}

object WeiboHelper {

  private val dict = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

  def uriToId(uri: String) = {
    val sb = groupFromEnd(uri, 4).foldLeft(new StringBuilder) {
      (sb, seg) => sb.append("%07d".format(decode62(seg).toInt))
    }
    sb.toLong
  }

  /**
    * Convert from digit id to weibo uri, uri means the last part of a status http://weibo.com/userId/uri.
    * <strong>This method may not work with some statuses published long ago</strong>
    * @param id id of the status
    * @return last part of the status url
    */
  def idToUri(id: Long) = {
    encodeSegements(id)
  }

  // cannot do tail optimize, not use this method when the string is too long
  private def groupFromEnd(str: String, size: Int): Seq[String] = {
    if (str.size <= size) Seq(str)
    else {
      groupFromEnd(str.substring(0, str.size - size), size) :+ str.substring(str.size - size, str.size)
    }
  }

  // cannot do tail optimize, not use this method when the string is too long
  private def encodeSegements(id: Long): String = {
    val seg = 10000000
    if (id < seg) {
      encode62(id)
    } else {
      encodeSegements(id / seg) + encodeAndPad(id % seg)
    }
  }

  private def encodeAndPad(id: Long) = {
    val str = encode62(id)
      ("0000" + str).takeRight(4)
  }

  private def decode62(str: String) = {
    str.reverse.zipWithIndex.foldLeft(BigInt(0)) {
      (sum, ci) =>
      val (c, i) = ci
      sum + BigInt(dict.indexOf(c)) * BigInt(62).pow(i)
    }
  }

  private def encode62(id: Long) = encode62Reverse(id, new StringBuilder).toString.reverse

  @scala.annotation.tailrec
  private def encode62Reverse(id: Long, sb: StringBuilder): StringBuilder = {
    sb.append(dict.charAt((id % 62).toInt))
    if (id >= 62) {
      encode62Reverse(id / 62, sb)
    } else {
      sb
    }

  }
}
