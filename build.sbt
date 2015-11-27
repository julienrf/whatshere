name := "whatshere"

version := "1"

scalaVersion := "2.11.7"

enablePlugins(PlayScala)

disablePlugins(PlayLayoutPlugin)

enablePlugins(SbtWeb)

LessKeys.rootpath := "/src/mail/assets/stylesheets/"

routesGenerator := InjectedRoutesGenerator

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)
