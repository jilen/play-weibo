package play.api

package object weibo {
  private[weibo] type P[A <: Api[R], R] = DefaultProtocol[A, R]
  private[weibo] type Get[A <: Api[R] , R] = HttpGetApi[A, R]
  private[weibo] type Post[A <: Api[R], R] = HttpPostApi[A, R]

  private[weibo] object url {
    def api(ns: String, name: String) = s"https://api.weibo.com/2/${ns}/${name}.json"
    def statuses(name: String) = api("statuses", name)
  }

  private[weibo] implicit object StatusesShowBatchP
      extends P[StatusesShowBatch, StatusesShowBatchResult]

  private[weibo] implicit object StatusesMentionP
      extends P[StatusesMentions, StatusesMentionsResult]

  private[weibo] implicit object StatusesUpdateP
      extends P[StatusesUpdate, Status]

  private[weibo] implicit object StatusesUploadP
      extends P[StatusesUpload, Status]

  private[weibo] implicit object StatusesTimelineBatchP
  extends P[StatusesTimelineBatch, StatusesTimelineBatchResult]
}
