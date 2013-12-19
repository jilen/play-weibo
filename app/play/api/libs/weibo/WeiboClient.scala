package play.api.libs.weibo

import java.text.SimpleDateFormat
import java.util.Date
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws._
import scala.concurrent._
import scala.concurrent.duration._

object WeiboClient {
  val timeout = 10.seconds
  val weiboDateFormat = "EEE MMM dd HH:mm:ss Z yyyy"

  private val logger = play.api.Logger(this.getClass)

  private[weibo] val apiHost = "https://api.weibo.com/2"
  private[weibo] val tokenUrl = "https://api.weibo.com/oauth2/access_token"
  private[this] val dict = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

  def oauthUrl(appClient: String, redirectUri: String) =
    "https://api.weibo.com/oauth2/authorize?client_id=" + appClient + "&response_type=code&redirect_uri=" + redirectUri

  def getAccessToken(appClient: String, appSecret: String, code: String, redirectUri: String) = {
    val future = post(tokenUrl, Seq(
      "client_id" -> appClient,
      "client_secret" -> appSecret,
      "grant_type" -> "authorization_code",
      "code" -> code,
      "redirect_uri" -> redirectUri)) map { resp =>
    }
  }



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


  /**
   * perform a get http(s) request
   */
  private def get[T](url: String, params: Seq[(String, String)])(implicit mf: Manifest[T]) = {
    val req = params.foldLeft(WS.url(url)) {
      (req, kv) => req.withQueryString(kv)
    }
    if (logger.isDebugEnabled) {
      logger.debug("weibo get request: " + req)
    }
    req.get()
  }

  private def post[T](url: String, params: Seq[(String, String)]) = {
    val nomarlized = params.groupBy(_._1).mapValues(_.map(_._2))
    WS.url(url).post(nomarlized)
  }
}
