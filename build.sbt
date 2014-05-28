name := "scala-exp"

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.google.http-client" % "google-http-client" % "1.18.0-rc" withSources() withJavadoc(),
  "com.googlecode.objectify" % "objectify" % "4.0b2" withSources() withJavadoc(),
  "com.google.oauth-client" % "google-oauth-client-servlet" % "1.18.0-rc" withSources() withJavadoc(),
  "com.google.oauth-client" % "google-oauth-client-appengine" % "1.18.0-rc" withSources() withJavadoc(),
  "com.google.apis" % "google-api-services-drive" % "v2-rev123-1.18.0-rc" withSources() withJavadoc(),
  "com.google.api-client" % "google-api-client" % "1.18.0-rc" withSources() withJavadoc(),
  "com.google.http-client" % "google-http-client-jackson" % "1.18.0-rc" withSources() withJavadoc(),
  "com.google.gdata" % "core" % "1.47.1" withSources() withJavadoc(),
  "junit" % "junit" % "4.11" % "test" withSources() withJavadoc(),
  "org.scalamock" % "scalamock-scalatest-support_2.11" % "3.1.1" % "test" withSources() withJavadoc(),
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.2" withSources() withJavadoc(),
  "org.mockito" % "mockito-all" % "1.9.5" % "test" withSources() withJavadoc()
)

lazy val felixtask = taskKey[Unit]("Test user-defined task")

felixtask := { println("Welcome to Felix's user-defined task") }

