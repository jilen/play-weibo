name := "play-weibo"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.5",
  "io.spray" % "spray-client" % "1.2.0",
  "com.typesafe.akka" %% "akka-actor" % "2.2.3",
  "org.scalatest" %% "scalatest" % "2.0")

scalacOptions ++= Seq("-feature", "-language:higherKinds")
