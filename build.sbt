name := "scala-exp"

version := "1.0"

scalaVersion := "2.10.3"

// t_2.10:3.0.1

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % "test" withSources() withJavadoc(),
  "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test" withSources() withJavadoc(),
  "org.mockito" % "mockito-all" % "1.9.5" % "test" withSources() withJavadoc()
)

lazy val felixtask = taskKey[Unit]("Test user-defined task")

felixtask := { println("Welcome to Felix's user-defined task") }

