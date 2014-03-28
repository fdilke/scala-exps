import sbt._
import Keys._

object Build extends Build {

  val buildSettings: Seq[Setting[_]] = Seq(
    retrieveManaged := true,
    managedDirectory    <<= baseDirectory(_ / "lib_managed"),
    retrievePattern     := "([classifier]/)[artifact](-[revision])(-[classifier]).[ext]",
    parallelExecution := false
  )

  val root = Project("ml-bulk-extractor", file("."))
    .settings(buildSettings: _*)

}

