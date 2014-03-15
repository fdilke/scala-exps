import sbt._
import sbt.Keys._

object Dependencies {

  lazy val dependencySettings = {
    Seq(
      libraryDependencies := dependencies,
      resolvers += "Remote artifacts repo" at "http://repo.tools.springer-sbm.com/artifacts/",
      resolvers += "Repo Tools Sonatype Nexus OSS Releases" at "http://repo.tools.springer-sbm.com:8081/nexus/content/repositories/releases/",
      resolvers += "Repo Tools Sonatype Nexus OSS Snapshots" at "http://repo.tools.springer-sbm.com:8081/nexus/content/repositories/snapshots/",
      resolvers += "Typesafe Repository" at "http://repo.akka.io/snapshots/"
    )
  }

  def dependencies() = Seq(
    "springer" % "casper-commons_2.10" % "0.139" withSources() changing() excludeAll(
      ExclusionRule(organization = "org.slf4j"),
      ExclusionRule(organization = "org.jboss.netty"),
      ExclusionRule(organization = "org.slf4j"),
      ExclusionRule(organization = "log4j"),
      ExclusionRule(organization = "springer", name = "http_2.10")
      ),
    "com.marklogic.reaktor" % "marklogic-xcc" % "4.2.6" from "http://repo.tools.springer-sbm.com/artifacts/com/marklogic/reaktor/marklogic-xcc/4.2.6/marklogic-xcc-4.2.6.jar",
    "com.typesafe.akka" %% "akka-actor" % "2.3-SNAPSHOT",
    "org.scala-lang" % "scala-reflect" % "2.10.3",
    "ch.qos.logback" % "logback-classic" % "1.0.9",
    "ch.qos.logback" % "logback-core" % "1.0.9",
    "org.slf4j" % "slf4j-api" % "1.7.2",
    "com.akamai.authentication" % "URLAuth-Java15" % "1.1.7" from "http://repo.tools.springer-sbm.com/artifacts/com/akamai/authentication/1.1.7/URLAuth-Java15.jar",
    "com.springer" %% "http" % "0.27" excludeAll( ExclusionRule(organization = "org.slf4j"), ExclusionRule(organization = "log4j") ) withSources() changing(),
    "com.springer" %% "http-test" % "0.3" changing()
  ) ++ testDependencies

 private val testDependencies = Seq(
    "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
    "org.mockito" % "mockito-all" % "1.9.5" % "test",
    "springer" %% "casper-commons-test-tools" % "0.39" withSources() changing(),
    "com.typesafe.akka" %% "akka-testkit" % "2.3-SNAPSHOT"
  )

}
