package com.rojoma.json.v3.jackson.fasterxml

import scala.beans._
import scala.annotation.meta._

import org.scalatest.{FunSuite, MustMatchers}
import org.scalatest.prop.PropertyChecks

import com.rojoma.json.v3.ast._
import com.rojoma.json.v3.testsupport.ArbitraryJValue._
import com.rojoma.json.v3.util.AutomaticJsonCodecBuilder
import com.rojoma.json.v3.interpolation._
import com.rojoma.json.v3.io.JsonReader

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation._
import com.fasterxml.jackson.core._

case class JsonableThing(x: Int, y: String)
object JsonableThing {
  implicit val jCodec = AutomaticJsonCodecBuilder[JsonableThing]
}

class JsonableThingSerializer extends ViaJValueSerializer[JsonableThing]
class JsonableThingDeserializer extends ViaJValueDeserializer[JsonableThing]

class ContainsJsonable @JsonCreator() (@BeanProperty @(JsonSerialize @beanGetter)(using=classOf[JsonableThingSerializer]) @(JsonDeserialize @param)(using=classOf[JsonableThingDeserializer]) @JsonProperty("v") val v: JsonableThing)

@JsonSerialize(using=classOf[OtherJsonableThingSerializer])
@JsonDeserialize(using=classOf[OtherJsonableThingDeserializer])
case class OtherJsonableThing(x: Int, y: String)
object OtherJsonableThing {
  implicit val jCodec = AutomaticJsonCodecBuilder[OtherJsonableThing]
}
class OtherJsonableThingSerializer extends ViaJValueSerializer[OtherJsonableThing]
class OtherJsonableThingDeserializer extends ViaJValueDeserializer[OtherJsonableThing]

class ContainsOtherJsonable @JsonCreator() (@BeanProperty @JsonProperty("v") val v: OtherJsonableThing)

class AnnotationTest extends FunSuite with MustMatchers {
  test("can serialize a thing via annotations on the field") {
    val om = new ObjectMapper
    val container = new ContainsJsonable(JsonableThing(5, "hello world!"))
    JsonReader.fromString(om.writeValueAsString(container)) must equal (json"{v:{x:5,y:'hello world!'}}")
  }

  test("can deserialize a thing via annotations on the field") {
    val om = new ObjectMapper
    val container = om.readValue(json"{v:{x:5,y:'hello world!'}}".toString, classOf[ContainsJsonable])
    container.v must equal (JsonableThing(5, "hello world!"))
  }

  test("can serialize a thing via annotations on the class of the field") {
    val om = new ObjectMapper
    val container = new ContainsOtherJsonable(OtherJsonableThing(5, "hello world!"))
    JsonReader.fromString(om.writeValueAsString(container)) must equal (json"{v:{x:5,y:'hello world!'}}")
  }

  test("can deserialize a thing via annotations on the class of the field") {
    val om = new ObjectMapper
    val container = om.readValue(json"{v:{x:5,y:'hello world!'}}".toString, classOf[ContainsOtherJsonable])
    container.v must equal (OtherJsonableThing(5, "hello world!"))
  }
}
