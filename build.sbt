import AssemblyKeys._

organization := "springer"

name := "scala-exp"

scalaVersion := "2.10.3"

version := Option(System.getenv("GO_PIPELINE_LABEL")).getOrElse("LOCAL")

resolvers += "Repo Tools Sonatype Nexus OSS Releases" at "http://repo.tools.springer-sbm.com:8081/nexus/content/repositories/releases/"

resolvers += "Remote artifacts repo" at "http://repo.tools.springer-sbm.com/artifacts/"

assemblySettings

jarName in assembly := "scala-exp.jar"

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case "about.html" => MergeStrategy.first
    case x => old(x)
  }
}

test in assembly := {}
