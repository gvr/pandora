
inThisBuild(
  Seq(
    organization := "com.github.gvr",
    scalaVersion := "2.12.8",
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xlint",
      "-Xfuture",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Ywarn-unused"
    )
  )
)

lazy val `pandora-lib` = (project in file("pandora-lib"))
  .settings(
    name := "pandora-lib",
    libraryDependencies ++= Dependencies.test ++
      Dependencies.akka ++
      Dependencies.logging ++
      Dependencies.monitor
  )

lazy val `pandora-sandbox` = (project in file("pandora-sandbox"))
  .dependsOn(`pandora-lib`)
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(
    name := "pandora-sandbox",
    libraryDependencies ++= Dependencies.test ++
      Dependencies.akka ++
      Dependencies.logging ++
      Dependencies.monitor
  )
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      version,
      scalaVersion,
      sbtVersion,
      BuildInfoKey.action("gitBranchName") {
        Utils.gitBranchName()
      },
      BuildInfoKey.action("gitCommitHash") {
        Utils.gitCommitHash()
      },
      BuildInfoKey.action("buildTime") {
        System.currentTimeMillis
      }
    ),
    buildInfoPackage := "pandora.sandbox.generated"
  )
