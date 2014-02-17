package play.api.weibo

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor._
import com.typesafe.config.ConfigFactory
import org.specs2.mutable._

abstract class ApiSpec extends Specification {
  val cfg = ConfigFactory.load("http.conf")
  val testToken = cfg.getString("token.normal")
  val testAdvancedToken = cfg.getString("token.advanced")

  implicit val http = new SprayHttp {
    val config = new SprayHttpConfig {
      val system = ActorSystem("test")
      val gzipEnable = true
    }
    val context = config.system.dispatcher
  }

  def awaitApi[A[R] <: Api[R], R](api: A[R]) = {
    Await.result(api.execute, Duration.Inf).right.get
  }
}
