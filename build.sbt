import sbtrelease._
import ReleaseKeys._
import ReleaseStateTransformations._

organization := "play-weibo"

name := "play-weibo"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.8",
  "com.ning" % "async-http-client" % "1.8.5",
  "io.spray" % "spray-client" % "1.2.0" % "provided",
  "com.typesafe.akka" %% "akka-actor" % "2.2.3" % "provided",
  "org.scalatest" %% "scalatest" % "2.0" % "test")

scalacOptions ++= Seq("-feature", "-language:higherKinds")

releaseSettings
