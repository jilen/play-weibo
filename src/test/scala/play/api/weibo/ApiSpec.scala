package play.api.weibo

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor._
import com.typesafe.config.ConfigFactory

trait ApiSpec {
  lazy val cfg = ConfigFactory.load("http.conf")
  lazy val testToken = cfg.getString("token.normal")
  val testAdvancedToken = cfg.getString("token.advanced")

  implicit val http = new SprayHttp {
    val config = new SprayHttpConfig {
      val system = ActorSystem("test")
      val gzipEnable = true
    }
  }

  def awaitApi[A <: Api[R], R, T](api: A)(block: R => T) = {   val res = Await.result(api.execute, Duration.Inf)
    block(res.right.get)
  }
}
