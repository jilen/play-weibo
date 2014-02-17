package play.api.weibo

import java.lang.reflect._

trait ApiReader[T <: Api[_]] {
  def read[T](api: T): Map[String, Any]
}

trait ApiParser[R] {
  def parse(body: String):  Either[WeiboApiError, R]
}

trait Protocol[A <: Api[R], R] extends ApiReader[A] with ApiParser[R]

trait ReflectiveReader[A <: Api[_]] extends ApiReader[A] {
  def read[A](api: A) = {
    val fields = api.getClass.getDeclaredFields()
    val values = fields.map(f => f.getName -> fieldVaule(f, api))
    values.collect {
      case (name, Some(v)) => snakify(name) -> v
      case (name, v) if v != None => snakify(name) -> v
    }.toMap
  }

  def snakify(name: String) = "[A-Z\\d]".r.replaceAllIn(name, {m =>
    "_" + m.group(0).toLowerCase()
  })

  private def fieldVaule(f: Field, obj: Any) = {
    f.setAccessible(true)
    f.get(obj)
  }
}

trait Json4sParser[R] extends ApiParser[R] {
  import org.json4s._
  import org.json4s.native.Serialization.{read, write}
  import java.text.SimpleDateFormat
  import java.util.Date

  implicit val m: Manifest[R]
  val DateFormat = "EEE MMM dd HH:mm:ss Z yyyy"
  implicit val formats = dateFormats(new SimpleDateFormat(DateFormat))

  def parse(body: String) = {
    val json = native.JsonMethods.parse(body).camelizeKeys
    println(json)
    json.extract[Either[WeiboApiError, R]]
  }

  private def dateFormats(format: SimpleDateFormat): Formats = {
    new DefaultFormats {
      override val dateFormatter = format
    }
  }
}

class DefaultProtocol[A <: Api[R], R : Manifest](implicit val m: Manifest[R])
    extends Protocol[A, R] with ReflectiveReader[A] with Json4sParser[R]
