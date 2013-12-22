package play.api.libs.weibo
import play.api.libs.weibo._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.Date
package object api {

  val weiboDateFormat = "EEE MMM dd HH:mm:ss Z yyyy"
  implicit val GeoReads:Reads[Geo] = Json.reads[Geo]
  implicit val StatusReads: Reads[Status] = Json.reads[Status]
  implicit val DateReads: Reads[Date] = Reads.dateReads(weiboDateFormat)
  implicit val AccessTokenReads: Reads[AccessToken] = Json.reads[AccessToken]

  //Shit
  implicit val UserReads: Reads[User] = new Reads[User]{
    def reads(json: JsValue) = {
      try { JsSuccess(
        new User(
          (json \ "id").as[Long],
          (json \ "idstr").as[String],
          (json \ "screenName").as[String],
          (json \ "name").as[String],
          (json \ "province").as[String],
          (json \ "city").as[String],
          (json \ "location").as[String],
          (json \ "description").as[String],
          (json \ "url").as[Option[String]],
          (json \ "profileImageUrl").as[String],
          (json \ "profileUrl").as[String],
          (json \ "domain").as[String],
          (json \ "weihao").as[String],
          (json \ "gender").as[String],
          (json \ "followersCount").as[Int],
          (json \ "friendsCount").as[Int],
          (json \ "statusesCount").as[Int],
          (json \ "favouritesCount").as[Int],
          (json \ "createdAt").as[Date],
          (json \ "following").as[Boolean],
          (json \ "allowAllActMsg").as[Boolean],
          (json \ "geoEnabled").as[Boolean],
          (json \ "verified").as[Boolean],
          (json \ "verifiedType").as[Int],
          (json \ "remark").as[String],
          (json \ "status").as[Option[Status]],
          (json \ "allowAllComment").as[Boolean],
          (json \ "avatarLarge").as[String],
          (json \ "verifiedReason").as[String],
          (json \ "followMe").as[Boolean],
          (json \ "onlineStatus").as[Int],
          (json \ "biFollowersCount").as[Int],
          (json \ "lang").as[String],
          (json \ "star").as[Int],
          (json \ "mbtype").as[Int],
          (json \ "mbrank").as[Int],
          (json \ "blockWord").as[Int]))
      } catch {
        case e: Throwable =>
          JsError(Nil)
      }
    }
  }
}
