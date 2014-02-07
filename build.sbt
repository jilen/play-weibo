name := "play-weibo"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

play.Project.playScalaSettings

libraryDependencies ++= Seq(
  "org.json4s"  %% "json4s-native" % "3.2.5",
  "io.spray"    %  "spray-client"  % "1.2.0")

scalaSource in Compile := file("src/main/scala")

scalaSource in Test := file("src/test/scala")

sourceDirectories in Test := Seq(
  file("src/test/scala"),
  file("src/test/resources"))

scalacOptions ++= Seq("-feature")
