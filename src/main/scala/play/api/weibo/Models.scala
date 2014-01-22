package play.api.weibo

import java.util.Date

case class AccessToken(
  accessToken: String,
  expiresIn: Int,
  remindIn: String,
  uid: String)

case class Status(
  createdAt: Date,
  id: Long,
  pid: Option[Long],
  text: String,
  source: String,
  favorited: Boolean,
  truncated: Boolean,
  inReplyToStatusId: Option[String],
  inReplyToUserId: Option[String],
  inReplyToScreenName: Option[String],
  thumbnailPic: Option[String],
  bmiddlePic: Option[String],
  originalPic: Option[String],
  geo: Option[Geo],
  user: Option[User],
  retweetedStatus: Option[Status],
  repostsCount: Int,
  commentsCount: Int,
  attitudesCount: Option[Int],
  mlevel: Option[Int],
  visible: Map[String, Int])

case class DeletedStatus(
  createdAt: Date,
  id: Long,
  text: String,
  deleted: String
)

/**
  * TODO  get ride of 22 fields of case class if scala 2.11 release
  */
class User(
  val id: Long,
  val idstr: String,
  val screenName: String,
  val name: String,
  val province: String,
  val city: String,
  val location: String,
  val description: String,
  val url: Option[String],
  val profileImageUrl: String,
  val profileUrl: String,
  val domain: String,
  val weihao: String,
  val gender: String,
  val followersCount: Int,
  val friendsCount: Int,
  val statusesCount: Int,
  val favouritesCount: Int,
  val createdAt: Date,
  val following: Boolean,
  val allowAllActMsg: Boolean,
  val geoEnabled: Boolean,
  val verified: Boolean,
  val verifiedType: Int,
  val remark: String,
  val status: Option[Status],
  val allowAllComment: Boolean,
  val avatarLarge: String,
  val verifiedReason: String,
  val followMe: Boolean,
  val onlineStatus: Int,
  val biFollowersCount: Int,
  val lang: String,
  val star: Int,
  val mbtype: Int,
  val mbrank: Int,
  val blockWord: Int)


case class Geo(
  longitude: String,
  latitude: String,
  city: String,
  province: String,
  city_name: String,
  province_name: String,
  address: String,
  pinyin: String,
  more: String)

case class ApiRateLimit(
  api: String,
  limit: Int,
  limitTimeUnit: String,
  remainingHits: Int)

case class Comment(
  createdAt: Date,
  id: Long,
  text: String,
  source: String,
  user: User,
  mid: String,
  idstr: String,
  status: Status,
  replyComment: Option[Comment])

case class UserTag(
  id: Long,
  tags: Seq[Tag])
case class Tag(
  tagId: Long,
  tagName: String,
  weight: Int)
