name := "cerebro"

maintainer := "Leonardo Menezes <leonardo.menezes@xing.com>"

packageSummary := "Elasticsearch web admin tool"

packageDescription := """cerebro is an open source(MIT License) elasticsearch web admin tool built
  using Scala, Play Framework, AngularJS and Bootstrap."""

version := "0.9.4-dl"

scalaVersion := "2.13.11"

rpmVendor := "lmenezes"

rpmLicense := Some("MIT")

rpmUrl := Some("http://github.com/lmenezes/cerebro")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play"                    % "2.8.16",
  "com.typesafe.play" %% "play-json"               % "2.9.4",
  "com.typesafe.play" %% "play-slick"              % "5.1.0",
  "com.typesafe.play" %% "play-slick-evolutions"   % "5.1.0",
  "org.xerial"        %  "sqlite-jdbc"             % "3.43.2.2",
  "org.specs2"        %% "specs2-junit"  % "4.20.2" % "test",
  "org.specs2"        %% "specs2-core"   % "4.20.2" % "test",
  "org.specs2"        %% "specs2-mock"   % "4.20.2" % "test"
)

libraryDependencies += filters
libraryDependencies += ws
libraryDependencies += guice

lazy val root = (project in file(".")).
  enablePlugins(PlayScala, BuildInfoPlugin, LauncherJarPlugin, JDebPackaging, RpmPlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "models"
  )

sources in (Compile, doc) := Seq.empty

enablePlugins(JavaServerAppPackaging)
enablePlugins(SystemdPlugin)

pipelineStages := Seq(digest, gzip)

serverLoading := Some(ServerLoader.Systemd)
systemdSuccessExitStatus in Debian += "143"
systemdSuccessExitStatus in Rpm += "143"
linuxPackageMappings += packageTemplateMapping(s"/var/lib/${packageName.value}")() withUser((daemonUser in Linux).value) withGroup((daemonGroup in Linux).value)
