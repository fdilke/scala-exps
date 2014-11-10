package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import Matchers._

class MultipleConstructorsTest extends FunSpec {

  case class Foo(value: Int, text: String) {
    def this(value: Int) = this(value, value.toString)
    def this(text: String) = this(text.toInt, text)
  }

  describe("Multiple constructors") {
    it("let you provide some construction arguments and have the rest filled in") {
      new Foo(3) shouldBe new Foo(3, "3")
      new Foo("8") shouldBe new Foo(8, "8")
    }
  }
}
