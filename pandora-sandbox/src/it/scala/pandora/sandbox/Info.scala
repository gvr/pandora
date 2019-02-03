package pandora.sandbox

import pandora.sandbox.generated.BuildInfo


object Info extends App {

  println(s"> name: ${BuildInfo.name}")
  println(s"> version: ${BuildInfo.version}")
  println(s"> sbt version: ${BuildInfo.sbtVersion}")
  println(s"> scala version: ${BuildInfo.scalaVersion}")
  println(s"> git branch name: ${BuildInfo.gitBranchName}")
  println(s"> git commit hash: ${BuildInfo.gitCommitHash}")
  println(s"> build time: ${BuildInfo.buildTime}")

}
