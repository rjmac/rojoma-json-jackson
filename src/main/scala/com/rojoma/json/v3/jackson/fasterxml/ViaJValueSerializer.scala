package com.rojoma.json.v3.jackson
package fasterxml

import com.fasterxml.jackson.core.{JsonToken, JsonGenerator}
import com.fasterxml.jackson.databind.{JsonSerializer, SerializerProvider}
import com.rojoma.json.v3.codec.JsonEncode
import scala.reflect.ClassTag

abstract class ViaJValueSerializer[T : JsonEncode : ClassTag] extends JsonSerializer[T] {
  override def serialize(value: T, jgen: JsonGenerator, provider: SerializerProvider): Unit =
    JValueSerializer.serialize(JsonEncode.toJValue(value), jgen, provider)

  override val handledType = implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]]
}
