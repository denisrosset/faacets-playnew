name := "faacets-playnew"
organization := "com.faacets"

version := "0.14.1.0-SNAPSHOT"

val faacetsVersion = "0.14.1.1"

val alascVersion = "0.14.1.0"
val catsVersion = "0.9.0"
val circeVersion = "0.7.0"
val circeYamlVersion = "0.5.0"
val consolidateVersion = "0.3"
val fastParseVersion = "0.4.2"
val scalinVersion = "0.14.1.0"
val shapelessVersion = "2.3.2"
val spireVersion = "0.14.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.1"

resolvers ++= Seq(
  "bintray/denisrosset/maven" at "https://dl.bintray.com/denisrosset/maven",
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

libraryDependencies ++= Seq(
  "com.faacets" %% "faacets-core" % faacetsVersion,
  "com.faacets" %% "faacets-data" % faacetsVersion,
  "net.alasc" %% "alasc-core" % alascVersion,
  "org.typelevel" %% "cats" % catsVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-yaml" % circeYamlVersion,
  "com.faacets" %% "consolidate" % consolidateVersion,
  "com.lihaoyi" %% "fastparse" % fastParseVersion,
  "net.alasc" %% "scalin-core" % scalinVersion,
  "com.chuusai" %% "shapeless" % shapelessVersion,
  "org.typelevel" %% "spire" % spireVersion
)

enablePlugins(DockerPlugin)

// libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.faacets.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.faacets.binders._"
