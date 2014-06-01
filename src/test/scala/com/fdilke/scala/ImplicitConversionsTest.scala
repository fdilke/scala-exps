package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import Matchers._

class ImplicitConversionsTest extends FunSpec {
  describe("Implicit conversions") {
    it("work by importing an object converting values to a wrapper") {

      object SpecialMultImplicits {
        implicit def Int2SpecialMult(value : Int) =
          new SpecialMult(value)
      }

      class SpecialMult(value: Int) {
        def **(exponent: Int) = Seq.fill(exponent)(value)./:(1)(_ * _)
      }

      new SpecialMult(2).**(3) shouldBe 8

      import SpecialMultImplicits._

      2 ** 3 shouldBe 8
    }
  }
}
