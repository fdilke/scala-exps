package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import Matchers._
import scala.language.implicitConversions

class ImplicitConversionsTest extends FunSpec {

  describe("Implicit conversions") {
    it("work by importing an object converting values to a wrapper") {

      object SpecialMultImplicits {
        implicit def Int2SpecialMult(value : Int): SpecialMult =
          new SpecialMult(value)
      }

      class SpecialMult(value: Int) {
        def **(exponent: Int) = Seq.fill(exponent)(value)./:(1)(_ * _)
      }

      new SpecialMult(2).**(3) shouldBe 8

      import SpecialMultImplicits._

      2 ** 3 shouldBe 8
    }

    it("can add function methods to strings") {

      object FunctionalStrings {
        case class Widget(n: Int)

        implicit class FunctionalString(value: String) {
          def law(position: Widget) = value.substring(position.n, position.n + 1)
        }
      }

      import FunctionalStrings._
      ("Hello" law (Widget(3))) shouldBe "l"
    }
  }
}
