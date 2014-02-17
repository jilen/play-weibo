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
