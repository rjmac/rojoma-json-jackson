package com.rojoma.json.v3.jackson
package codehaus

import org.codehaus.jackson.{JsonGenerator, Version}
import org.codehaus.jackson.map.{JsonSerializer, SerializerProvider}
import com.rojoma.json.v3.codec.JsonEncode
import scala.reflect.ClassTag

abstract class ViaJValueSerializer[T : JsonEncode : ClassTag] extends JsonSerializer[T] {
  override def serialize(value: T, jgen: JsonGenerator, provider: SerializerProvider): Unit =
    JValueSerializer.serialize(JsonEncode.toJValue(value), jgen, provider)

  override val handledType = implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]]
}
