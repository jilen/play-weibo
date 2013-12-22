import sbt._
import Keys._
import play.Project._

object PlayWeiboBuild extends Build {

  lazy val macros = Project("macros", file("macros")) settings(
    libraryDependencies <+= scalaVersion(
      "org.scala-lang" % "scala-compiler" % _)
  )
  lazy val main = Project("play-weibo", file("."))
    .settings(play.Project.playScalaSettings:_*)
    .settings(scalaSource in Compile := file("src/main/scala"))
    .dependsOn(macros)
}
