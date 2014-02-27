package play.api.weibo

case class StatusesShowBatch(
  accessToken: String,
  ids: String,
  trimUser: Option[Int] = None
) extends HttpGetApi[StatusesShowBatch, StatusesShowBatchResult](url.statuses("show_batch"))

object StatusesShowBatch {
  val TrimUser = Some(1)
  val NotTrimUser = Some(0)
}

case class StatusesMentions(
  accessToken: String,
  sinceId: Option[Long] = None,
  maxId: Option[Long] = None,
  count: Option[Int] = None,
  page: Option[Int] = None,
  filterByAuthor: Option[Int] = None,
  filterBySource: Option[Int] = None,
  filterByType: Option[Int] = None)
    extends Get[StatusesMentions, StatusesMentionsResult](url.statuses("mentions"))

case class StatusesUpdate(
  val accessToken: String,
  val status: String,
  val lat: Option[String] = None,
  val long: Option[String] = None,
  val annotation: Option[String] = None)
    extends Post[StatusesUpdate, Status](url.statuses("update"))
