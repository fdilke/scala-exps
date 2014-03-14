import sbt._
import Keys._
import Dependencies._

object Build extends Build {

  val buildSettings: Seq[Setting[_]] = Seq(
    retrieveManaged := true,
    managedDirectory    <<= baseDirectory(_ / "lib_managed"),
    retrievePattern     := "([classifier]/)[artifact](-[revision])(-[classifier]).[ext]",
    parallelExecution := false
  ) ++ dependencySettings ++ ZipTasks.settings

  val root = Project("ml-bulk-extractor", file("."))
    .settings(buildSettings: _*)

}

object ZipTasks {
  lazy val settings = (zipTests <<= zipTestsTask)
  private val zipTests = TaskKey[Unit]("zip-tests", "Creates an artifact which contains integration, functional  tests.")
  private def zipTestsTask = {
    (baseDirectory, sourceDirectory in Compile, fullClasspath in Runtime, fullClasspath in Test, packageBin in Compile, packageBin in Test, streams) map {
      (base, sourceDirectory, runtimeDeps, testDeps, coreJar, testJar, streams) =>

        val libs = (runtimeDeps.files +++ testDeps.files +++ coreJar +++ testJar).get x flatRebase("lib")
        val runScript: Seq[(File, String)] = (base / "ops" / "ci" / "run-tests.sh") x rebase(base / "ops" / "ci", "")
        val runTaskScript = (base / "ops" / "deploy" / "run-task.sh") x rebase(base / "ops" / "deploy", "ops/deploy")
        val properties = (base / "ops" / "ci" / "app.ci.properties") x rebase(base / "ops" / "ci", "ops/ci")
        val zipPath = coreJar.getAbsolutePath.replace(".jar", "-tests.zip")
        IO.zip(libs ++ runScript ++ properties ++ runTaskScript, file(zipPath))
        streams.log.info("Created zip: " + zipPath)
    }
  }
}
