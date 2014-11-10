name := "scala-exp"

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.google.http-client" % "google-http-client" % "1.18.0-rc",
  "com.googlecode.objectify" % "objectify" % "4.0b2",
  "com.google.oauth-client" % "google-oauth-client-servlet" % "1.18.0-rc",
  "com.google.oauth-client" % "google-oauth-client-appengine" % "1.18.0-rc",
  "com.google.apis" % "google-api-services-drive" % "v2-rev123-1.18.0-rc",
  "com.google.api-client" % "google-api-client" % "1.18.0-rc",
  "com.google.http-client" % "google-http-client-jackson" % "1.18.0-rc",
  "com.google.gdata" % "core" % "1.47.1",
  "junit" % "junit" % "4.11" % "test",
  "org.scalamock" % "scalamock-scalatest-support_2.11" % "3.1.1" % "test",
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.2",
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  "org.scala-lang" % "scala-compiler" % "2.11.3",
  "org.scala-lang" % "scala-library" % "2.9.0-1"
).map { _.withSources().withJavadoc() }

lazy val felixtask = taskKey[Unit]("Test user-defined task")

felixtask := { println("Welcome to Felix's user-defined task") }

