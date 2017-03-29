name := "faacets-playnew"
organization := "com.faacets"

version := "0.14.1.0-SNAPSHOT"

val faacetsVersion = "0.14.1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.1"

resolvers ++= Seq(
  "bintray/denisrosset/maven" at "https://dl.bintray.com/denisrosset/maven",
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

libraryDependencies ++= Seq(
  "com.faacets" %% "faacets-core" % faacetsVersion,
  "com.faacets" %% "faacets-data" % faacetsVersion
)

enablePlugins(DockerPlugin)

// libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.faacets.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.faacets.binders._"
