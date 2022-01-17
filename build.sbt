Global / semanticdbEnabled := true
Global / onChangedBuildSource := ReloadOnSourceChanges
import java.io.File


resolvers += "4 jvm repr" at "https://maven.scijava.org/content/repositories/public/"

lazy val root = project
  .in(file("."))
  .settings(
    name := "example",
    description := "dedav example",    
    scalaVersion := "2.13.6",
    scalacOptions ++= Seq(
        "-Ytasty-reader" // to use scala 3 lib
    ),
    libraryDependencies ++= Seq(
      ("io.github.quafadas" %% "dedav4s" % "0.1.1").cross(CrossVersion.for2_13Use3),
    )
  )