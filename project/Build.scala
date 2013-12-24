import sbt._
import Keys._
import play.Project._

object PlayWeiboBuild extends Build {

  lazy val macros = Project("macros", file("macros"))
    .settings(play.Project.playScalaSettings:_*)
    .settings(libraryDependencies <+= scalaVersion(
      "org.scala-lang" % "scala-compiler" % _),
      scalaSource in Compile := file("macros/src/main/scala"))

  lazy val main = Project("play-weibo", file("."))
    .settings(play.Project.playScalaSettings:_*)
    .settings(scalaSource in Compile := file("src/main/scala"))
    .dependsOn(macros)
}
