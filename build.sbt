
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

lazy val root = (project in file("."))
  .settings(
    name := "pandora"
  )

lazy val `pandora-core` = (project in file("pandora-core"))
  .settings(
    name := "pandora-core",
    libraryDependencies ++= Dependencies.test
  )
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](
      BuildInfoKey.map(name) { case (k, _) => k -> "pandora" },
      version,
      scalaVersion,
      sbtVersion,
      BuildInfoKey.action("gitBranchName") {
        Utils.gitBranchName()
      },
      BuildInfoKey.action("gitCommitHash") {
        Utils.gitCommitHash()
      }
    ),
    buildInfoOptions += BuildInfoOption.BuildTime,
    buildInfoPackage := "pandora.core.generated"
  )

lazy val `pandora-lib` = (project in file("pandora-lib"))
  .dependsOn(`pandora-core`)
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
