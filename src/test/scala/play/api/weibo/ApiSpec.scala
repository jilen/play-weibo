package play.api.weibo

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor._
import com.typesafe.config.ConfigFactory
import org.scalatest.FlatSpec
import com.ning.http.client._


abstract class ApiSpec extends FlatSpec {
  val cfg = ConfigFactory.load("http.conf")
  val testToken = cfg.getString("token.normal")
  val testAdvancedToken = cfg.getString("token.advanced")

  val config = new SprayHttpConfig {
    val system = ActorSystem("test")
    val gzipEnabled = true
  }

  implicit val http = {
    val cfgBuilder = new AsyncHttpClientConfig.Builder().setCompressionEnabled(true)
    AsyncHttp.withConfig(
      context = scala.concurrent.ExecutionContext.global,
      cfg = cfgBuilder.build()
    )
  }

  def awaitApi[A[R] <: Api[R], R](api: A[R]) = {
    Await.result(api.execute, Duration.Inf)
  }
}
