package pandora.core

import java.lang.management.{ManagementFactory, RuntimeMXBean}

import pandora.core.generated.BuildInfo
import pandora.core.time._

object MetaData {

  @transient private val runtimeBean: RuntimeMXBean = ManagementFactory.getRuntimeMXBean

  val startTime: String = formatDateFromInstant(runtimeBean.getStartTime)

  val vmName: String = runtimeBean.getVmName

  val vmVersion: String = runtimeBean.getVmVersion

  val name: String = BuildInfo.name

  val buildTime: String = formatDateFromInstant(BuildInfo.builtAtMillis)

  val gitBranchName: Option[String] = BuildInfo.gitBranchName

  val gitCommitHash: Option[String] = BuildInfo.gitCommitHash

  val sbtVersion: String = BuildInfo.sbtVersion

  val scalaVersion: String = BuildInfo.scalaVersion

  val version: String = BuildInfo.version

}
