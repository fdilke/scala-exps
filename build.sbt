name := "scala-exp"

version := "1.0"

scalaVersion := "2.13.1"

scalacOptions ++= Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
  "com.google.cloud" % "google-cloud-datastore" % "1.102.0" excludeAll(
    ExclusionRule("io.grpc", "grpc-core"),
    ExclusionRule("com.google.cloud","google-cloud-core-http")
  ),
  "com.google.http-client" % "google-http-client" % "1.34.0" excludeAll(
    ExclusionRule("io.opencensus", "opencensus-contrib-http-util")
  ),
  "com.googlecode.objectify" % "objectify" % "6.0.5" excludeAll(
    ExclusionRule("com.google.cloud", "google-cloud-datastore")
  ),
  "com.google.oauth-client" % "google-oauth-client-appengine" % "1.30.5",
  "com.google.apis" % "google-api-services-drive" % "v3-rev20191108-1.30.3",
  "com.google.api-client" % "google-api-client" % "1.30.7" excludeAll(
    ExclusionRule("io.grpc", "grpc-core"),
    ExclusionRule("com.google.http-client", "google-http-client-jackson2")
  ),
  "com.google.http-client" % "google-http-client-jackson" % "1.29.2",
  "io.opencensus" % "opencensus-contrib-http-util" % "0.24.0" excludeAll(
        ExclusionRule("com.google.guava", "guava")
  ),
  "com.google.gdata" % "core" % "1.47.1" excludeAll (
    ExclusionRule("com.google.guava", "guava")
  ),
  "com.google.http-client" % "google-http-client-jackson2" % "1.34.0" excludeAll(
    ExclusionRule("com.google.api", "api-common")
  ),
//  "org.typesafe.akka" % "akka" % "2.2.0-RC2",
  // com.google.api:gax-httpjson:0.66.1
  "com.google.cloud" % "google-cloud-core-http" % "1.92.1" excludeAll (
    ExclusionRule("com.google.api", "api-common")
  ),
  "com.google.api" % "api-common" % "1.8.1" excludeAll (
    ExclusionRule("com.google.guava", "guava")
  ),
  "junit" % "junit" % "4.13" % "test",
//  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test",
  "org.scalatest" %% "scalatest" % "3.2.0-M2" excludeAll(
    ExclusionRule("org.scala-lang.modules", "scala-xml_2.13"),
  ),
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "org.scala-lang.modules" %% "scala-xml" % "2.0.0-M1",
  "org.mockito" %% "mockito-scala" % "1.10.2" % "test",
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scala-lang" % "scala-library" % scalaVersion.value,
  "org.scalameta" %% "scalameta" % "4.3.0",
  "com.typesafe.play" %% "play-json" % "2.8.1" excludeAll
    ExclusionRule("org.scala-lang"),
  "javax.xml.bind" % "jaxb-api" % "2.4.0-b180830.0359"
//  "com.github.gilbertw1" %% "slack-scala-client" % "0.2.2",
//  "org.typelevel" %% "cats" % "0.9.0"
).map {
  _.withSources().withJavadoc()
} ++ Seq(
  "org.apache.jena" % "apache-jena-libs" % "3.13.1" pomOnly() excludeAll(
    ExclusionRule("org.apache.thrift", "libthrift")
  )
//  "org.apache.jena" % "apache-jena-libs" % "3.13.0" excludeAll ExclusionRule("org.scala-lang")
//  "org.apache.jena" % "jena-jdbc-driver-bundle" % "3.1.0" excludeAll ExclusionRule(organization = "org.slf4j")
)

lazy val felixtask = taskKey[Unit]("Test user-defined task")

felixtask := { println("Welcome to Felix's user-defined task") }

