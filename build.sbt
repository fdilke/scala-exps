name := "scala-exp"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.google.http-client" % "google-http-client" % "1.20.0",
  "com.googlecode.objectify" % "objectify" % "5.1.5",
  "com.google.oauth-client" % "google-oauth-client-servlet" % "1.20.0",
  "com.google.oauth-client" % "google-oauth-client-appengine" % "1.20",
  "com.google.apis" % "google-api-services-drive" % "v2-rev171-1.19.0",
  "com.google.api-client" % "google-api-client" % "1.20.0",
  "com.google.http-client" % "google-http-client-jackson" % "1.20.0",
  "com.google.gdata" % "core" % "1.47.1",
  "junit" % "junit" % "4.12" % "test",
  "org.scalamock" % "scalamock-scalatest-support_2.11" % "3.1.1" % "test",
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  "org.scala-lang" % "scala-compiler" % "2.11.6",
  "org.scala-lang" % "scala-library" % "2.11.6"
).map { _.withSources().withJavadoc() }

lazy val felixtask = taskKey[Unit]("Test user-defined task")

felixtask := { println("Welcome to Felix's user-defined task") }

