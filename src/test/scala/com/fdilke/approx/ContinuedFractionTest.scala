package com.fdilke.approx

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class ContinuedFractionTest extends AnyFunSpec {
  describe("A continued fraction") {
    it("of depth 0 has no coefficients") {
      ContinuedFraction(3.14, 0) should have(
        'coefficients(Seq()),
        'approximants(Seq())
      )
    }

    it("of depth 1 is just the integer part") {
      ContinuedFraction(3.14, 1) should have(
        'coefficients(Seq(3)),
        'approximants(Seq(Rational(3, 1)))
      )
    }

    it("can be created to specified depth for a number") {
      ContinuedFraction(1.19, 2) should have(
        'coefficients(Seq(1, 5)),
        'approximants(Seq(Rational(1, 1), Rational(6, 5)))
      )
    }
  }
}
