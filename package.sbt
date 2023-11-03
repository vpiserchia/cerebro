import Common.remapPath
// Install service files
linuxPackageMappings ++= Seq(
  packageMapping(
    file("conf/cerebro.service") -> "/usr/lib/systemd/system/cerebro.service"
  ).withPerms("644")
)

daemonUser := "cerebro"
bashScriptEnvConfigLocation := None

