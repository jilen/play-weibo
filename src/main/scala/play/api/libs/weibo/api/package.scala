package play.api.libs.weibo
import play.api.libs.weibo._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.Date
import scala.language.experimental.macros

package object api {

  val weiboDateFormat = "EEE MMM dd HH:mm:ss Z yyyy"
  private[weibo] def paramReads[A] = macro Macros.readParamsImpl[A]

  implicit val StatusesShowReads = paramReads[StatusesShow]

  private[weibo] def readsUnderscore[A] = macro Macros.readJsonImpl[A]

  implicit val DateReads: Reads[Date] = Reads.dateReads(weiboDateFormat)
  implicit val GeoReads:Reads[Geo] = Json.reads[Geo]
  implicit val StatusReads: Reads[Status] = Json.reads[Status]
  implicit val AccessTokenReads: Reads[AccessToken] = readsUnderscore[AccessToken]

  //Shit
  implicit val UserReads: Reads[User] = new Reads[User]{
    def reads(json: JsValue) = {
      try { JsSuccess(
        new User(
          (json \ "id").as[Long],
          (json \ "idstr").as[String],
          (json \ "screen_name").as[String],
          (json \ "name").as[String],
          (json \ "province").as[String],
          (json \ "city").as[String],
          (json \ "location").as[String],
          (json \ "description").as[String],
          (json \ "url").as[Option[String]],
          (json \ "profile_image_url").as[String],
          (json \ "profile_url").as[String],
          (json \ "domain").as[String],
          (json \ "weihao").as[String],
          (json \ "gender").as[String],
          (json \ "followers_count").as[Int],
          (json \ "friends_count").as[Int],
          (json \ "statuses_count").as[Int],
          (json \ "favourites_count").as[Int],
          (json \ "created_at").as[Date],
          (json \ "following").as[Boolean],
          (json \ "allow_all_act_msg").as[Boolean],
          (json \ "geo_enabled").as[Boolean],
          (json \ "verified").as[Boolean],
          (json \ "verified_type").as[Int],
          (json \ "remark").as[String],
          (json \ "status").as[Option[Status]],
          (json \ "allow_all_comment").as[Boolean],
          (json \ "avatar_large").as[String],
          (json \ "verified_reason").as[String],
          (json \ "follow_me").as[Boolean],
          (json \ "online_status").as[Int],
          (json \ "bi_followers_count").as[Int],
          (json \ "lang").as[String],
          (json \ "star").as[Int],
          (json \ "mbtype").as[Int],
          (json \ "mbrank").as[Int],
          (json \ "block_word").as[Int]))
      } catch {
        case e: Throwable =>
          JsError(Nil)
      }
    }
  }
}
