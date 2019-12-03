// import com.typesafe.tools.mima.plugin.MimaPlugin.mimaDefaultSettings
// import com.typesafe.tools.mima.plugin.MimaKeys.previousArtifact

// mimaDefaultSettings

organization := "com.rojoma"

name := "rojoma-json-v3-jackson"

version := "1.0.1"

mimaPreviousArtifacts := Set("com.rojoma" % ("rojoma-json-v3-jackson_" + scalaBinaryVersion.value) % "1.0.0")

libraryDependencies ++= Seq(
  "com.rojoma" %% "rojoma-json-v3" % "[3.1.2,4.0.0)",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.2" % "optional",
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13" % "optional",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"
)

scalacOptions ++= Seq("-feature", "-deprecation")

scalaVersion := "2.13.1"

crossScalaVersions := Seq("2.10.4", "2.11.2", "2.12.8", scalaVersion.value)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")

resourceGenerators in Compile += Def.task {
  val baseDir = (resourceManaged in Compile).value
  val artifactId = (name in Compile).value
  val groupId = (organization in Compile).value
  val sbv = (scalaBinaryVersion in Compile).value
  val ver = (version in Compile).value

  import com.rojoma.json.v3.interpolation._
  import com.rojoma.json.v3.ast._
  import com.rojoma.json.v3.io._
  val file = baseDir / "com" / "rojoma" / "json" / "v3" / "jackson" / "version-info.json"
  file.getParentFile.mkdirs()
  val VersionRegex="""(\d+)\.(\d+)\.(\d+)(?:-(SNAPSHOT))?""".r
  val (major,minor,micro,frag) = ver match {
    case VersionRegex(maj, min, mic, null) => (maj.toInt, min.toInt, mic.toInt, None)
    case VersionRegex(maj, min, mic, f) => (maj.toInt, min.toInt, mic.toInt, Some(f))
    case _ => sys.error("Cannot parse version")
  }
  val json = j"""
      { artifactId: ${artifactId + "_" + sbv}
      , groupId: $groupId
      , major: $major
      , minor: $minor
      , micro: $micro
      , frag: ${frag.map(JString).getOrElse(JNull) : JValue}
      }"""
  val o = new java.io.FileOutputStream(file)
  try {
    val w = new java.io.OutputStreamWriter(o, java.nio.charset.StandardCharsets.UTF_8)
    PrettyJsonWriter.toWriter(w, json)
    w.close()
  } finally {
    o.close()
  }
  Seq(file)
}
