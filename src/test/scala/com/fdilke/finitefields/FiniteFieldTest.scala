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

    it("can load the integers mod p") {
      val field5: FiniteField = FiniteField.GF(5)
    }
  }
}
