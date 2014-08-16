package com.rojoma.json.v3.jackson
package fasterxml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{JsonDeserializer, JsonMappingException, DeserializationContext}
import com.rojoma.json.v3.codec.JsonDecode

abstract class ViaJValueDeserializer[T : JsonDecode] extends JsonDeserializer[T] {
  override def deserialize(jp: JsonParser, ctxt: DeserializationContext): T = {
    val loc = ctxt.getParser.getCurrentLocation
    JsonDecode.fromJValue[T](JValueDeserializer.deserialize(jp, ctxt)) match {
      case Right(r) => r
      case Left(err) => throw new JsonMappingException(err.english, loc)
    }
  }
}
