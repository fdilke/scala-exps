name := "scala-exp"

version := "1.0"

scalaVersion := "2.12.3"

scalacOptions ++= Seq("-feature", "-deprecation", "-Xexperimental")

libraryDependencies ++= Seq(
  "com.google.http-client" % "google-http-client" % "1.22.0",
  "com.googlecode.objectify" % "objectify" % "5.1.21",
  "com.google.oauth-client" % "google-oauth-client-appengine" % "1.22.0",
  "com.google.apis" % "google-api-services-drive" % "v2-rev276-1.21.0",
  "com.google.api-client" % "google-api-client" % "1.22.0",
  "com.google.http-client" % "google-http-client-jackson" % "1.22.0",
  "com.google.gdata" % "core" % "1.47.1",
  "junit" % "junit" % "4.12" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test",
  "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",
  "org.mockito" % "mockito-all" % "1.10.19" % "test",
  "org.scala-lang" % "scala-compiler" % "2.12.3",
  "org.scala-lang" % "scala-library" % "2.12.3",
  "org.scalameta" %% "scalameta" % "1.8.0"
).map {
  _.withSources().withJavadoc()
} ++ Seq(
  "org.apache.jena" % "apache-jena-libs" % "3.4.0" excludeAll ExclusionRule("org.scala-lang")
//  "org.apache.jena" % "jena-jdbc-driver-bundle" % "3.1.0" excludeAll ExclusionRule(organization = "org.slf4j")
)

lazy val felixtask = taskKey[Unit]("Test user-defined task")

felixtask := { println("Welcome to Felix's user-defined task") }

