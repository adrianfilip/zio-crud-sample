name := "ziosample"

organization := "adrianfilip"
version := "0.0.1"

val zio_version   = "1.0.0-RC18-2"
scalaVersion := "2.13.1"

lazy val config = (project in file("."))
  .settings(
    //DEPENDENCIES
    libraryDependencies += "dev.zio" %% "zio" % zio_version,
    //json stuff
    libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.7.0-M1"
  )
