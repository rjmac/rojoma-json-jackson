package com.rojoma.json.v3.jackson
package fasterxml

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.module.SimpleModule
import com.rojoma.json.v3.ast._

object Module extends SimpleModule("rojoma-json-v3", RJJVersion.version(new Version(_, _, _, _, _, _))) {
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
