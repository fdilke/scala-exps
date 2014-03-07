name := "scala-exp"

version := "1.0"

scalaVersion := "2.10.2"

// libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

// libraryDependencies += "junit" % "junit" % "4.10" % "test"

libraryDependencies += "junit" % "junit" % "4.11" % "test"

// t_2.10:3.0.1

libraryDependencies +=
  "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test"

lazy val felixtask = taskKey[Unit]("Test user-defined task")

felixtask := { println("Welcome to Felix's user-defined task") }

