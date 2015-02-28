package com.fdilke.approx

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class RationalTest extends FunSpec {
  describe("Rational numbers") {
    it("can be created from integers or pairs of integers") {
      Rational(6) shouldBe Rational(6, 1)
    }

    it("are brought to canonical form automatically") {
      val r = Rational(4 , 6)
      r shouldBe Rational(2, 3)
    }

    it("can be added") {
      Rational(1, 2) + Rational(1, 3) shouldBe Rational(5, 6)
      Rational(1, 2) + Rational(1, 3) + Rational(1, 6) shouldBe Rational.ONE
    }

    it("can be multiplied") {
      Rational(1, 2) * Rational(2, 1) shouldBe Rational.ONE
      Rational(1, 2) * Rational(1, 3) shouldBe Rational(1, 6)
    }

    it("can be divided") {
      Rational(1, 2) / Rational(3, 4) shouldBe Rational(2, 3)
    }
  }
}
