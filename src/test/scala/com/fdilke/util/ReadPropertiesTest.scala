package com.fdilke.util

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class ReadPropertiesTest extends AnyFunSpec {
  describe("ReadProperties") {
    it("loads properties from a file") {
      ReadProperties("src/test/resources/sample.config") shouldBe
        Map("key1" -> "value1",
            "key2" -> "value2")
    }
    it("fails if the file format is wrong") {
      intercept[IllegalArgumentException] {
        ReadProperties("src/test/resources/bad-format.config")
      }
    }
    it("allows blank lines and comments") {
      ReadProperties("src/test/resources/with-comments-and-whitespace.config") shouldBe
        Map("key" -> "value")
    }
  }
}