name := "scala-exp"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.google.apis" % "google-api-services-drive" % "v2-rev123-1.18.0-rc",
  "com.google.api-client" % "google-api-client" % "1.18.0-rc",
  "com.google.http-client" % "google-http-client-jackson" % "1.18.0-rc",
  "com.google.gdata" % "core" % "1.47.1" withSources() withJavadoc(),
  "junit" % "junit" % "4.11" % "test" withSources() withJavadoc(),
  "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test" withSources() withJavadoc(),
  "org.mockito" % "mockito-all" % "1.9.5" % "test" withSources() withJavadoc()
)

lazy val felixtask = taskKey[Unit]("Test user-defined task")

felixtask := { println("Welcome to Felix's user-defined task") }

