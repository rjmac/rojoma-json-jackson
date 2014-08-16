package com.rojoma.json.v3.jackson
package fasterxml

import com.fasterxml.jackson.core.{JsonToken, JsonParser, JsonGenerator}
import com.fasterxml.jackson.databind.{JsonSerializer, SerializerProvider}
import com.rojoma.json.v3.ast._

class JAtomSerializer extends JsonSerializer[JAtom] {
  override def serialize(value: JAtom, jgen: JsonGenerator, provider: SerializerProvider): Unit = {
    value match {
      case s: JString =>
        JStringSerializer.serialize(s, jgen, provider)
      case n: JNumber =>
        JNumberSerializer.serialize(n, jgen, provider)
      case b: JBoolean =>
        JBooleanSerializer.serialize(b, jgen, provider)
      case n: JNull =>
        JNullSerializer.serialize(n, jgen, provider)
    }
  }
  override val handledType = classOf[JAtom]
}
object JAtomSerializer extends JAtomSerializer

class JCompoundSerializer extends JsonSerializer[JCompound] {
  override def serialize(value: JCompound, jgen: JsonGenerator, provider: SerializerProvider): Unit = {
    value match {
      case obj: JObject =>
        JObjectSerializer.serialize(obj, jgen, provider)
      case arr: JArray =>
        JArraySerializer.serialize(arr, jgen, provider)
    }
  }
  override val handledType = classOf[JCompound]
}
object JCompoundSerializer extends JCompoundSerializer

class JValueSerializer extends JsonSerializer[JValue] {
  override def serialize(value: JValue, jgen: JsonGenerator, provider: SerializerProvider): Unit = {
    value match {
      case compound: JCompound =>
        JCompoundSerializer.serialize(compound, jgen, provider)
      case atom: JAtom =>
        JAtomSerializer.serialize(atom, jgen, provider)
    }
  }
  override val handledType = classOf[JValue]
}
object JValueSerializer extends JValueSerializer

class JObjectSerializer extends JsonSerializer[JObject] {
  override def serialize(value: JObject, jgen: JsonGenerator, provider: SerializerProvider): Unit = {
    jgen.writeStartObject()
    for((k, v) <- value) {
      jgen.writeFieldName(k)
      JValueSerializer.serialize(v, jgen, provider)
    }
    jgen.writeEndObject()
  }
  override val handledType = classOf[JObject]
}
object JObjectSerializer extends JObjectSerializer

class JArraySerializer extends JsonSerializer[JArray] {
  override def serialize(value: JArray, jgen: JsonGenerator, provider: SerializerProvider): Unit = {
    jgen.writeStartArray()
    value.foreach(JValueSerializer.serialize(_, jgen, provider))
    jgen.writeEndArray()
  }
  override val handledType = classOf[JArray]
}
object JArraySerializer extends JArraySerializer

class JNumberSerializer extends JsonSerializer[JNumber] {
  override def serialize(value: JNumber, jgen: JsonGenerator, provider: SerializerProvider): Unit =
    jgen.writeNumber(value.toJBigDecimal)
  override val handledType = classOf[JNumber]
}
object JNumberSerializer extends JNumberSerializer

class JStringSerializer extends JsonSerializer[JString] {
  override def serialize(value: JString, jgen: JsonGenerator, provider: SerializerProvider): Unit =
    jgen.writeString(value.string)
  override val handledType = classOf[JString]
}
object JStringSerializer extends JStringSerializer

class JBooleanSerializer extends JsonSerializer[JBoolean] {
  override def serialize(value: JBoolean, jgen: JsonGenerator, provider: SerializerProvider): Unit =
    jgen.writeBoolean(value.boolean)
  override val handledType = classOf[JBoolean]
}
object JBooleanSerializer extends JBooleanSerializer

class JNullSerializer extends JsonSerializer[JNull] {
  override def serialize(value: JNull, jgen: JsonGenerator, provider: SerializerProvider): Unit =
    jgen.writeNull()
  override val handledType = classOf[JNull]
}
object JNullSerializer extends JNullSerializer
