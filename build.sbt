import sbtassembly.Plugin._
import AssemblyKeys._

assemblySettings

name := "GPSProcessing"

version := "0.1"

organization := "Abrams Inc."

scalaVersion := "2.10.1"

// Resolvers and libraries
resolvers ++= Seq("sonatype-public"     at "https://oss.sonatype.org/content/groups/public")

libraryDependencies ++= { Seq(
    // Options
    "com.github.scopt"                %% "scopt"               % "2.1.0",
    //Convert, handles joda conversions
    "org.joda" % "joda-convert" % "1.2",
    // Time, better datetime handling
    "joda-time"                       % "joda-time"            % "2.1",
    //Mail Agent
    "javax.mail"                      % "mail"              % "1.4.6"
  )
}

mainClass := Some("CLI")
