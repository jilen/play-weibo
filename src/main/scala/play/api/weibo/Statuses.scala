package play.api.weibo

case class StatusesShowBatch(
) extends HttpGetApi[StatusesShowBatch, StatusesShowBatchResult](url.statuses("show_batch"))