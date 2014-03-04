package play.api.weibo

import com.ning.http.client.{AsyncHttpClient, AsyncHttpClientConfig}
import com.ning.http.client.{Request, Response}
import scala.concurrent.ExecutionContext
import scala.concurrent.Promise
import com.ning.http.client.{FilePart, StringPart}

class AsyncHttp(cfg: AsyncHttpConfig) extends Http {

  import cfg.{asyncHttpClient => cli}
  def get(url: String, params: Map[String, Any]) = {
    val builder = params.foldLeft(cli.prepareGet(url)) {
      case (builder, (name, value)) =>
        builder.addQueryParameter(name, value.toString)
    }
    execute(builder.build())
  }



  def post(url: String, params: Map[String, Any]) = {
    val isMultiPart = params.exists { p =>
      val (name, value) = p
      value.isInstanceOf[java.io.File]
    }

    val builder = if(isMultiPart) {
      params.foldLeft(cli.preparePost(url)) {
        case (builder, (name, value: java.io.File)) =>
          builder.addBodyPart(new FilePart(name, value, null, null))
        case (builder, (name, value)) =>
          builder.addBodyPart(new StringPart(name, value.toString))
      }
    } else {
      params.foldLeft(cli.preparePost(url)) {
        case (builder, (name, value)) =>
          builder.addParameter(name, value.toString)
      }
    }
    execute(builder.build())
  }

  val context: ExecutionContext = cfg.context

  private def execute(request: Request) = {
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
}

case class AsyncHttpConfig(
  context: ExecutionContext,
  asyncHttpClient: AsyncHttpClient
)

object AsyncHttp {

  def withConfig(context: ExecutionContext, cfg: AsyncHttpClientConfig) = {
    new AsyncHttp(
      AsyncHttpConfig(
        context = scala.concurrent.ExecutionContext.global,
        asyncHttpClient = new AsyncHttpClient(cfg)
      )
    )
  }
}
