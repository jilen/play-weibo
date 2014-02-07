package play.api

package object weibo {
  private[weibo] type P[A <: Api[R], R] = DefaultProtocol[A, R]
  private[weibo] type Get[A <: Api[R] , R] = HttpGetApi[A, R]
  private[weibo] type Post[A <: Api[R], R] = HttpPostApi[A, R]



  private[weibo] object url {
    def api(ns: String, name: String) = s"https://api.weibo.com/2/${ns}/${name}.json"
    def statuses(name: String) = api("statuses", name)
  }


  private[weibo] implicit object StatusShowBatchProtocol
      extends P[StatusesShowBatch, StatusesShowBatchResult]
}
