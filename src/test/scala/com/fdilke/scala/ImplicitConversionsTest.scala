package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

import scala.language.implicitConversions

class ImplicitConversionsTest extends AnyFunSpec {

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
      ("Hello" law Widget(3)) shouldBe "l"
    }

    it("can use implicit parameters, too") {
      case class Widget[T](payload: T)
      object FancyImplicits {
        implicit class RichWidget[T](
          widget: Widget[T]
        ) (
          implicit toInt: T => Int
        ) {
          def measure: Int =
            toInt(widget.payload)
        }
      }
      import FancyImplicits._

      implicit val shoeSize: String => Int =
        _ => 6

      Widget("Felix").measure shouldBe 6
    }

    it("work from inside a biproduct class") {
      class BiproductProxy[S, T] {
        val list = List[(S, T)]()
        object implicits {
          implicit class RichLeftElement(
            s: S
          ) {
            def ⊕⊕(t: T) =
              list
          }
        }
      }
      val biproduct = new BiproductProxy[Int, String]
      import biproduct.implicits._
      27 ⊕⊕ "xx" shouldBe Seq()
      (new biproduct.implicits.RichLeftElement(27)).⊕⊕("xx") shouldBe Seq()
    }
  }
}
