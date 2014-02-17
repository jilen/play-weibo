package play.api.weibo

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor._
import com.typesafe.config.ConfigFactory
import org.scalatest.FlatSpec

abstract class ApiSpec extends FlatSpec {
  val cfg = ConfigFactory.load("http.conf")
  val testToken = cfg.getString("token.normal")
  val testAdvancedToken = cfg.getString("token.advanced")

  val config = new SprayHttpConfig {
    val system = ActorSystem("test")
    val gzipEnable = true
  }

  implicit val http = new SprayHttp(config)

  def awaitApi[A[R] <: Api[R], R](api: A[R]) = {
    Await.result(api.execute, Duration.Inf)
  }
}
