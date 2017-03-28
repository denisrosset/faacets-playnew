name := """faacets-playnew"""
organization := "com.faacets"

version := "1.0-SNAPSHOT"

val faacetsVersion = "0.13.1.2"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.1"

resolvers ++= Seq(
  Resolver.url("spirejars", url(file("spirejars").toURI.toASCIIString))(Resolver.ivyStylePatterns), // TODO: remove use of Spire prerelease
  "bintray/non" at "http://dl.bintray.com/non/maven",
  "bintray/denisrosset/maven" at "https://dl.bintray.com/denisrosset/maven",
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

libraryDependencies += filters

libraryDependencies ++= Seq(
  "com.faacets" %% "faacets-core" % faacetsVersion,
  "com.faacets" %% "faacets-data" % faacetsVersion
)

// libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.faacets.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.faacets.binders._"
