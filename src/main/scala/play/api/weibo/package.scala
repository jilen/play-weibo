package play.api

package object weibo {
  type P[A <: Api[R], R] = DefaultProtocol[A, R]
  private[weibo] implicit object StatusShowBatchProtocol
      extends P[StatusesShowBatch, StatusesShowBatchResult]
}
