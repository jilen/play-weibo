package play.api.libs.weibo.api

import play.api.libs.weibo._

case class StatusShow(
  accessToken: String, id: Long)
    extends GetApi[Status]("https://api.weibo.com/2/statuses/show.json")
