package com.fdilke.finitefields

import org.scalatest.{Matchers, FunSpec}
import Matchers._

class FiniteFieldTest extends FunSpec {
  describe("The finite field arithmetic library") {
    it("can recognize nontrivial prime powers") {
      val NontrivialPrimePower(q, r) = 7
      q shouldBe 7
      r shouldBe 1

      val NontrivialPrimePower(p, n) = 81
      p shouldBe 3
      n shouldBe 4

      intercept[MatchError] {
        val NontrivialPrimePower(p, n) = 1
      }

      intercept[MatchError] {
        val NontrivialPrimePower(p, n) = 6
      }
    }

    it("rejects request for a non-prime-power-sized field") {
      intercept[IllegalArgumentException] {
        FiniteField.GF(6)
      }
    }

    it("can load the integers mod 5") {
      val field5: FiniteField = FiniteField.GF(5)

      import field5._
      field5 should have size 5
      field5.toSet shouldBe Set(
        O, I, I+I, I+I+I, I+I+I+I
      )
      (I + I) * (I + I + I) shouldBe I
    }

    it("can load GF(4)") {
      val field4 = FiniteField.GF(4)

      import field4._
      field4 should have size 4
      val Seq(o, i, a, b) = field4.toSeq
      o shouldBe O
      i shouldBe I
      b shouldBe (a + I)
      a shouldBe (b + I)
      (a * a) shouldBe b
      (b * b) shouldBe a
    }

    it("can load GF(27)") {
      testField(27)
    }

    it("can load GF(81)") {
      testField(81)
    }

    /* these work, but take too long to run

    ignore("can load GF(243)") {
      testField(243)
    }

    ignore("can load GF(343)") {
      testField(343)
    }

    ignore("can load GF(1024)") {
      testField(1024)
    }
    */

    def testField(pn: Int) {
      val field = FiniteField.GF(pn)
      import field._
      field should have size pn
      field foreach { a =>
        a + O shouldBe a
        a + (-a) shouldBe O
        a * O shouldBe O
        a * I shouldBe a
        if (a != O) {
          a * ~a shouldBe I
        }
        field foreach { b =>
          a + b shouldBe b + a
          a * b shouldBe b * a
          (a - b) + b shouldBe a
          if (b != O) {
            a/b * b shouldBe a
          }
          field foreach { c =>
            (a + b) + c shouldBe ( a + (b + c) )
            (a * b) * c shouldBe ( a * (b * c) )
            (a + b) * c shouldBe ( (a * c) + (b * c) )
          }
        }
      }
    }
  }
}
