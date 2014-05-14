package com.fdilke.util

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import ShouldMatchers._

class ReadPropertiesTest extends FunSpec {
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

object Dummy extends App {
  private val Comment = "\\#.*".r
  private val Whitespace = "\\s*".r

  "# xx" match {
    case Comment() => println("Yes")
    case _ => println("No")
  }
}