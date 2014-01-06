package play.api.libs.weibo.api

import play.api.libs.weibo._

case class StatusesShow(
  accessToken: String,
  id: Long
) extends GetApi[StatusesShow,Status]("https://api.weibo.com/2/statuses/show.json")
