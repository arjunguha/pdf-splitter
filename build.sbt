organization := "edu.umass.cs"
scalaVersion := "2.11.7"
scalacOptions ++=
 Seq("-deprecation",
     "-unchecked",
     "-feature",
     "-Xfatal-warnings")

libraryDependencies ++=
  Seq("org.apache.pdfbox" % "pdfbox" % "1.8.10",
      "ch.qos.logback" % "logback-classic" % "1.0.9",
      "ch.qos.logback" % "logback-core" % "1.0.9",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0")

assemblyJarName in assembly := "makelonepdfs.jar"
test in assembly := {}

