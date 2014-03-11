package play.api.weibo

import java.io.File

object StatusesConstant {
  val TrimUser = Some(1)
  val NotTrimUser = Some(0)
}

case class StatusesTimelineBatch(
  accessToken: String,
  uids: Option[String] = None,
  screenNames: Option[String] = None,
  page: Option[Int] = None,
  count: Option[Int] = None,
  baseApp: Option[Int] = None,
  feature: Option[Int] = None,
  trimUser: Option[Int] = None)
    extends Get[StatusesTimelineBatch, StatusesTimelineBatchResult](url.statuses("timeline_batch"))

case class StatusesShowBatch(
  accessToken: String,
  ids: String,
  trimUser: Option[Int] = None
) extends HttpGetApi[StatusesShowBatch, StatusesShowBatchResult](url.statuses("show_batch"))


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

case class StatusesUpload(
  accessToken: String,
  status: String,
  visible: Option[Int] = None,
  list_id: Option[String] = None,
  pic: File,
  lat: Option[Float] = None,
  long: Option[Float] = None,
  annotations: Option[String] = None,
  rip: Option[String] = None) extends Post[StatusesUpload, Status](url.statuses("upload"))
