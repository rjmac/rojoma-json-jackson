rojoma-json-v3-jackson
======================

Jackson interop for rojoma-json-v3
----------------------------------

This package provides modules that can be added to a Jackson
`ObjectMapper` in order to have it serialize and deserialize
rojoma-json `JValue`s.  It provides implementations for both
codehaus.org Jackson and fasterxml.com Jackson.

```scala
import scala.beans.BeanProperty
import com.rojoma.json.v3.ast.JValue
import com.rojoma.json.v3.interpolation._
import org.codehaus.jackson.annotate.{JsonCreator, JsonProperty}
import org.codehaus.jackson.map.ObjectMapper
import com.rojoma.json.v3.jackson.codehaus.Module // or .fasterxml.Module

case class Foo @JsonCreator() (
  @JsonProperty("someValue") @BeanProperty someValue: JValue
)

val mapper = new ObjectMapper
mapper.registerModule(Module)

val foo = Foo(j"{hello:'world'}")
mapper.writeValueAsString(foo) // == {"someValue":{"hello":"world"}}
mapper.readValue("""{"someValue":{"hello":"world"}}""", classOf[Foo]) // == foo
```

It also provides classes that can be used to create serializers and
deserializers for things with rojoma-json `JsonEncode`s and
`JsonDecode`s.

```scala
import scala.beans.BeanProperty
import com.rojoma.json.v3.ast.JValue
import com.rojoma.json.v3.util._
import org.codehaus.jackson.annotate.{JsonCreator, JsonProperty}
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.map.annotate.{JsonSerialize, JsonDeserialize}
import com.rojoma.json.v3.jackson.codehaus._ // or .fasterxml._

@JsonSerialize(using=classOf[JsonableThingSerializer])
@JsonDeserialize(using=classOf[JsonableThingDeserializer])
case class JsonableThing(x: Int, y: String)
object JsonableThing {
  implicit val jCodec = AutomaticJsonCodecBuilder[JsonableThing]
}
class JsonableThingSerializer extends ViaJValueSerializer[JsonableThing]
class JsonableThingDeserializer extends ViaJValueDeserializer[JsonableThing]

case class Foo @JsonCreator() (
  @JsonProperty("someValue") @BeanProperty someValue: JsonableThing
)

val mapper = new ObjectMapper
val foo = Foo(JsonableThing(42, "hello!"))
mapper.writeValueAsString(foo) // == {"someValue":{"x":42,"y":"hello!"}}
mapper.readValue("""{"someValue":{"x":42,"y":"hello!"}}""", classOf[Foo]) // == foo
```
