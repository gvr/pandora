package pandora.sandbox

import pandora.sandbox.generated.BuildInfo

object VersionInfo {

  private val startTime = System.currentTimeMillis()

  lazy val asMap: Map[String, String] = Map(
    "name" -> BuildInfo.name,
    "version" -> BuildInfo.version,
    "branch" -> BuildInfo.gitBranchName.getOrElse("<none>"),
    "commit" -> BuildInfo.gitCommitHash.getOrElse("<none>"),
    "build-time" -> new java.util.Date(BuildInfo.buildTime).toString,
    "start-time" -> new java.util.Date(startTime).toString
  )

}
