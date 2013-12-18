name := "play-weibo"

version := "0.1.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.scala-lang"               % "scala-compiler" % "2.10.3",
  "org.scala-lang"               % "scala-reflect" % "2.10.3")

scalacOptions ++= Seq("-feature")

publishTo <<= version { (v: String) =>
  val repo = "http://maven.jilen.org/nexus"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at repo + "content/repositories/snapshots")
  else
    Some("releases"  at repo + "content/repositories/releases")
}

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

play.Project.playScalaSettings
