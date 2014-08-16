package com.rojoma.json.v3.jackson
package codehaus

import org.codehaus.jackson.{JsonToken, JsonParser}
import org.codehaus.jackson.map.{JsonDeserializer, DeserializationContext}
import com.rojoma.json.v3.ast._

import scala.collection.mutable

class JObjectDeserializer extends JsonDeserializer[JObject] {
  override def deserialize(jp: JsonParser, ctxt: DeserializationContext): JObject = {
    if(jp.getCurrentToken != JsonToken.START_OBJECT) throw ctxt.mappingException(classOf[JObject])
    if(jp.nextToken() == JsonToken.END_OBJECT) return JObject.canonicalEmpty
    val result = new mutable.LinkedHashMap[String, JValue]
    while(jp.getCurrentToken != JsonToken.END_OBJECT) {
      val k = jp.getCurrentName
      jp.nextToken()
      val v = JValueDeserializer.deserialize(jp, ctxt)
      jp.nextToken()
      result += k -> v
    }
    JObject(result)
  }
}
object JObjectDeserializer extends JObjectDeserializer

class JArrayDeserializer extends JsonDeserializer[JArray] {
  override def deserialize(jp: JsonParser, ctxt: DeserializationContext): JArray = {
    if(jp.getCurrentToken != JsonToken.START_ARRAY) throw ctxt.mappingException(classOf[JArray])
    if(jp.nextToken() == JsonToken.END_ARRAY) return JArray.canonicalEmpty
    val result = Vector.newBuilder[JValue]
    while(jp.getCurrentToken != JsonToken.END_ARRAY) {
      val v = JValueDeserializer.deserialize(jp, ctxt)
      jp.nextToken()
      result += v
    }
    JArray(result.result())
  }
}
object JArrayDeserializer extends JArrayDeserializer

class JStringDeserializer extends JsonDeserializer[JString] {
  override def deserialize(jp: JsonParser, ctxt: DeserializationContext): JString = {
    if(jp.getCurrentToken != JsonToken.VALUE_STRING) throw ctxt.mappingException(classOf[JString])
    JString(jp.getText)
  }
}
object JStringDeserializer extends JStringDeserializer

class JBooleanDeserializer extends JsonDeserializer[JBoolean] {
  override def deserialize(jp: JsonParser, ctxt: DeserializationContext): JBoolean = {
    jp.getCurrentToken match {
      case JsonToken.VALUE_FALSE => JBoolean.canonicalFalse
      case JsonToken.VALUE_TRUE => JBoolean.canonicalTrue
      case _ => throw ctxt.mappingException(classOf[JBoolean])
    }
  }
}
object JBooleanDeserializer extends JBooleanDeserializer

class JNumberDeserializer extends JsonDeserializer[JNumber] {
  override def deserialize(jp: JsonParser, ctxt: DeserializationContext): JNumber = {
    jp.getCurrentToken match {
      case JsonToken.VALUE_NUMBER_INT => JNumber.unsafeFromString(jp.getText)
      case JsonToken.VALUE_NUMBER_FLOAT => JNumber.unsafeFromString(jp.getText)
      case _ => throw ctxt.mappingException(classOf[JNumber])
    }
  }
}
object JNumberDeserializer extends JNumberDeserializer

class JNullDeserializer extends JsonDeserializer[JNull] {
  override def deserialize(jp: JsonParser, ctxt: DeserializationContext): JNull = {
    if(jp.getCurrentToken != JsonToken.VALUE_NULL) throw ctxt.mappingException(classOf[JNull])
    JNull
  }
  override val getNullValue = JNull
}
object JNullDeserializer extends JNullDeserializer

class JValueDeserializer extends JsonDeserializer[JValue] {
  override def deserialize(jp: JsonParser, ctxt: DeserializationContext): JValue = {
    jp.getCurrentToken match {
      case JsonToken.START_OBJECT => JObjectDeserializer.deserialize(jp, ctxt)
      case JsonToken.START_ARRAY => JArrayDeserializer.deserialize(jp, ctxt)
      case JsonToken.VALUE_STRING => JStringDeserializer.deserialize(jp, ctxt)
      case JsonToken.VALUE_FALSE | JsonToken.VALUE_TRUE => JBooleanDeserializer.deserialize(jp, ctxt)
      case JsonToken.VALUE_NUMBER_INT | JsonToken.VALUE_NUMBER_FLOAT => JNumberDeserializer.deserialize(jp, ctxt)
      case JsonToken.VALUE_NULL => JNullDeserializer.deserialize(jp, ctxt)
      case _ => throw ctxt.mappingException(classOf[JValue])
    }
  }
  override val getNullValue = JNull
}
object JValueDeserializer extends JValueDeserializer

class JCompoundDeserializer extends JsonDeserializer[JCompound] {
  override def deserialize(jp: JsonParser, ctxt: DeserializationContext): JCompound = {
    jp.getCurrentToken match {
      case JsonToken.START_OBJECT => JObjectDeserializer.deserialize(jp, ctxt)
      case JsonToken.START_ARRAY => JArrayDeserializer.deserialize(jp, ctxt)
      case _ => throw ctxt.mappingException(classOf[JCompound])
    }
  }
}
object JCompoundDeserializer extends JCompoundDeserializer

class JAtomDeserializer extends JsonDeserializer[JAtom] {
  override def deserialize(jp: JsonParser, ctxt: DeserializationContext): JAtom = {
    jp.getCurrentToken match {
      case JsonToken.VALUE_STRING => JStringDeserializer.deserialize(jp, ctxt)
      case JsonToken.VALUE_FALSE | JsonToken.VALUE_TRUE => JBooleanDeserializer.deserialize(jp, ctxt)
      case JsonToken.VALUE_NUMBER_INT | JsonToken.VALUE_NUMBER_FLOAT => JNumberDeserializer.deserialize(jp, ctxt)
      case JsonToken.VALUE_NULL => JNullDeserializer.deserialize(jp, ctxt)
      case _ => throw ctxt.mappingException(classOf[JValue])
    }
  }
  override val getNullValue = JNull
}
object JAtomDeserializer extends JAtomDeserializer
