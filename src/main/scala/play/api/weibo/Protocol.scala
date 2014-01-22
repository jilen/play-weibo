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
    fields.map(f => f.getName -> fieldVaule(f, api)).toMap
  }

  private def fieldVaule(f: Field, obj: Any) = {
    f.setAccessible(true)
    f.get(obj)
  }
}

trait Json4sParser[R] extends ApiParser[R] {
  import org.json4s._
  import org.json4s.native.Serialization.{read, write}
  implicit val formats = native.Serialization.formats(NoTypeHints)
  implicit val m: Manifest[R]
  def parse(body: String) = {
    read[Either[WeiboApiError, R]](body)
  }
}

class DefaultProtocol[A <: Api[R], R : Manifest](implicit val m: Manifest[R])
    extends Protocol[A, R] with ReflectiveReader[A] with Json4sParser[R]
