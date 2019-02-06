package pandora.sandbox

import pandora.core.MetaData

object VersionInfo {

  lazy val asMap: Map[String, String] = Map(
    "name" -> MetaData.name,
    "version" -> MetaData.version,
    "git-branch" -> MetaData.gitBranchName.getOrElse("<none>"),
    "git-commit" -> MetaData.gitCommitHash.getOrElse("<none>"),
    "build-time" -> MetaData.buildTime,
    "start-time" -> MetaData.startTime
  )

}
