package com.rojoma.json.v3.jackson
package codehaus

import org.codehaus.jackson.JsonParser
import org.codehaus.jackson.map.{JsonDeserializer, DeserializationContext, JsonMappingException}
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
