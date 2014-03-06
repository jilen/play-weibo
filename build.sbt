import sbtrelease._
import ReleaseKeys._
import ReleaseStateTransformations._
import com.typesafe.sbt.pgp.PgpKeys._

organization := "play-weibo"

name := "play-weibo"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.5",
  "com.ning" % "async-http-client" % "1.7.24",
  "io.spray" % "spray-client" % "1.2.0" % "provided",
  "com.typesafe.akka" %% "akka-actor" % "2.2.3" % "provided",
  "org.scalatest" %% "scalatest" % "2.0" % "test")

scalacOptions ++= Seq("-feature", "-language:higherKinds")

releaseSettings

releaseProcess := Seq(
  checkSnapshotDependencies,
  inquireVersions,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseTask(publishSigned),
  setNextVersion,
  commitNextVersion,
  pushChanges)

//Publish setttings
publishMavenStyle := true

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/jilen/play-weibo</url>
  <licenses>
    <license>
    <name>BSD-style</name>
    <url> http://www.opensource.org/licenses/bsd-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>https://github.com/jilen/play-weibo.git</url>
    <connection>scm:git:https://github.com/jilen/play-weibo.git</connection>
  </scm>
  <developers>
    <developer>
      <id>jsuereth</id>
      <name>jilen</name>
      <url>http://jilen.github.io</url>
    </developer>
  </developers>
)
