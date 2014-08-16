package com.rojoma.json.v3.jackson

import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

import com.rojoma.json.v3.util.{AutomaticJsonDecodeBuilder, JsonUtil}

private [jackson] object RJJVersion {
  def version[T](vBuilder: (Int, Int, Int, String, String, String) => T): T = {
    case class Info(major: Int, minor: Int, micro: Int, frag: Option[String], groupId: String, artifactId: String)
    implicit val codec = AutomaticJsonDecodeBuilder[Info]
    val i = getClass.getResourceAsStream("version-info.json")
    try {
      JsonUtil.readJson[Info](new InputStreamReader(i, StandardCharsets.UTF_8)) match {
        case Right(info) => vBuilder(info.major, info.minor, info.micro, info.frag.orNull, info.groupId, info.artifactId)
        case Left(err) => sys.error("Unable to read version info: " + err.english)
      }
    } finally {
      i.close()
    }
  }
}
