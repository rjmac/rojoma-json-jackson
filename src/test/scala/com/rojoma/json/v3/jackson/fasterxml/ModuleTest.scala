package com.rojoma.json.v3.jackson.fasterxml

import org.scalatest.{FunSuite, MustMatchers}
import org.scalatest.matchers.{Matcher, MatchResult}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import com.rojoma.json.v3.ast._
import com.rojoma.json.v3.testsupport.ArbitraryJValue._

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core._

class ModuleTest extends FunSuite with MustMatchers with ScalaCheckPropertyChecks {
  test("Serializing value must roundtrip") {
    val om = new ObjectMapper
    om.registerModule(Module)
    forAll { (v: JValue) =>
      om.readValue(om.writeValueAsString(v), classOf[JValue]) must equal (v)
    }
  }

  test("Serializing compound must roundtrip") {
    val om = new ObjectMapper
    om.registerModule(Module)
    forAll { (v: JCompound) =>
      om.readValue(om.writeValueAsString(v), classOf[JCompound]) must equal (v)
    }
  }

  test("Serializing atom must roundtrip") {
    val om = new ObjectMapper
    om.registerModule(Module)
    forAll { (v: JAtom) =>
      om.readValue(om.writeValueAsString(v), classOf[JAtom]) must equal (v)
    }
  }

  test("Serializing object must roundtrip") {
    val om = new ObjectMapper
    om.registerModule(Module)
    forAll { (v: JObject) =>
      om.readValue(om.writeValueAsString(v), classOf[JObject]) must equal (v)
    }
  }

  test("Serializing array must roundtrip") {
    val om = new ObjectMapper
    om.registerModule(Module)
    forAll { (v: JArray) =>
      om.readValue(om.writeValueAsString(v), classOf[JArray]) must equal (v)
    }
  }

  test("Serializing string must roundtrip") {
    val om = new ObjectMapper
    om.registerModule(Module)
    forAll { (v: JString) =>
      om.readValue(om.writeValueAsString(v), classOf[JString]) must equal (v)
    }
  }

  test("Serializing number must roundtrip") {
    val om = new ObjectMapper
    om.registerModule(Module)
    forAll { (v: JNumber) =>
      om.readValue(om.writeValueAsString(v), classOf[JNumber]) must equal (v)
    }
  }

  test("Serializing boolean must roundtrip") {
    val om = new ObjectMapper
    om.registerModule(Module)
    forAll { (v: JBoolean) =>
      om.readValue(om.writeValueAsString(v), classOf[JBoolean]) must equal (v)
    }
  }

  test("Serializing null must roundtrip") {
    val om = new ObjectMapper
    om.registerModule(Module)
    forAll { (v: JNull) =>
      om.readValue(om.writeValueAsString(v), classOf[JNull]) must equal (v)
    }
  }
}
