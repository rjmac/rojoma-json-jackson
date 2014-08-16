package com.rojoma.json.v3.jackson
package codehaus

import org.codehaus.jackson.Version
import org.codehaus.jackson.map.module.SimpleModule
import com.rojoma.json.v3.ast._

object Module extends SimpleModule("rojoma-json-v3", RJJVersion.version((maj,min,mic,frag,_,_) => new Version(maj,min,mic,frag))) {
  addSerializer(JObjectSerializer)
  addSerializer(JArraySerializer)
  addSerializer(JNumberSerializer)
  addSerializer(JStringSerializer)
  addSerializer(JBooleanSerializer)
  addSerializer(JNullSerializer)

  addDeserializer(classOf[JObject], JObjectDeserializer)
  addDeserializer(classOf[JArray], JArrayDeserializer)
  addDeserializer(classOf[JString], JStringDeserializer)
  addDeserializer(classOf[JBoolean], JBooleanDeserializer)
  addDeserializer(classOf[JNumber], JNumberDeserializer)
  addDeserializer(classOf[JNull], JNullDeserializer)
  addDeserializer(classOf[JCompound], JCompoundDeserializer)
  addDeserializer(classOf[JAtom], JAtomDeserializer)
  addDeserializer(classOf[JValue], JValueDeserializer)
}
