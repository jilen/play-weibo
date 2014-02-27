package play.api.weibo

object Generator {
  private val dict = "abcdefghijklmnopqrstuvwxyz0123456789"

  def randomString(size: Int) = {
    val rnd = new scala.util.Random
    def randomChar = dict.charAt(rnd.nextInt(dict.length))
    new String(Array.fill(size)(randomChar))
  }
}
