package play.api.weibo

import com.ning.http.client.{AsyncHttpClient, AsyncHttpClientConfig}
import com.ning.http.client.{Request, Response}
import scala.concurrent.ExecutionContext
import scala.concurrent.Promise
import com.ning.http.client.{FilePart, StringPart}
import org.jboss.netty.handler.codec.embedder.CodecEmbedderException

class AsyncHttp(cfg: AsyncHttpConfig) extends Http {
  import cfg._
  implicit val context: ExecutionContext = cfg.context

  def get(url: String, params: Map[String, Any]) = {
    execute(prepareGet(url,params))
  }

  def post(url: String, params: Map[String, Any]) = {
    execute(preparePost(url, params))
  }

  private def prepareGet(url: String, params: Map[String, Any]) = {
    val builder = params.foldLeft(gziped.prepareGet(url)) {
      case (builder, (name, value)) =>
        builder.addQueryParameter(name, value.toString)
    }
    builder.build()
  }

  private def preparePost(url: String, params: Map[String, Any]) = {
    val isMultiPart = params.exists { p =>
      val (name, value) = p
      value.isInstanceOf[java.io.File]
    }
    val builder = if(isMultiPart) {
      params.foldLeft(gziped.preparePost(url)) {
        case (builder, (name, value: java.io.File)) =>
          builder.addBodyPart(new FilePart(name, value, null, null))
        case (builder, (name, value)) =>
          builder.addBodyPart(new StringPart(name, value.toString))
      }
    } else {
      params.foldLeft(gziped.preparePost(url)) {
        case (builder, (name, value)) =>
          builder.addParameter(name, value.toString)
      }
    }
    builder.build()
  }



  private def executeRequest(cli: AsyncHttpClient, request: Request) = {
    import com.ning.http.client.AsyncCompletionHandler
    val result = Promise[String]()
    cli.executeRequest(request, new AsyncCompletionHandler[Response] {
      override def onCompleted(response: Response) =  {
        result.success(response.getResponseBody())
        response
      }
    })
    result.future
  }

  private def execute(request: Request) = {
    executeRequest(gziped, request).recoverWith {
      case e: CodecEmbedderException =>
        executeRequest(plain, request)
    }
  }
}

case class AsyncHttpConfig(
  context: ExecutionContext,
  gziped: AsyncHttpClient,
  plain: AsyncHttpClient
)

object AsyncHttp {

  def withConfig(
    context: ExecutionContext,
    cfg: AsyncHttpClientConfig.Builder) = {
    new AsyncHttp(
      AsyncHttpConfig(
        context = context,
        gziped = new AsyncHttpClient(cfg.setCompressionEnabled(true).build()),
        plain = new AsyncHttpClient(cfg.setCompressionEnabled(false).build())
      )
    )
  }

  def withClient(
    context: ExecutionContext,
    plain: AsyncHttpClient,
    gziped: AsyncHttpClient) =  {
    new AsyncHttp(
      AsyncHttpConfig(
        context = context,
        gziped = gziped,
        plain = plain)
    )
  }
}
