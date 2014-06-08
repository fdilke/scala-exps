package com.fdilke.bewl.fsets

import org.scalatest.{Matchers, FunSpec}
import Matchers._
import com.fdilke.bewl.fsets.FiniteSets.FiniteSetsDot

// User: Felix Date: 07/06/2014 Time: 12:28

class FiniteSetsExponentialTest extends FunSpec {
  describe("The exponential") {
    describe("has the correct number of values, all distinct") {
      val source = FiniteSetsDot('a, 'b, 'c)
      val target = FiniteSetsDot(1, 2, 3, 4)
      val expDot = target ^ source
      expDot.set.toSet should have ('size (64))
    }
  }
}
