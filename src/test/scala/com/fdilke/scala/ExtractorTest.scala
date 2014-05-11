package com.fdilke.scala

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import ShouldMatchers._

// User: Felix Date: 11/05/2014 Time: 10:31

class ExtractorTest extends FunSpec {
  describe("extractors") {
    it("can be invoked from regexes") {
      val propertyRegex = "(.*?)=(.*)".r
      val property = "shoeSize=5"

      val propertyRegex(key, value) = property
      key shouldBe "shoeSize"
      value shouldBe "5"
    }

    it("can be applied to extract a single value") {
      object Twice {
        def unapply(n: Int): Option[Int] = if (n % 2 == 0) Some(n / 2) else None
      }

      val Twice(m) = 6
      m shouldBe 3

      intercept[MatchError] {
        val Twice(q) = 5
      }
    }

    it("can be applied to extract several values") {
      object PrimePower {
        def unapply(n : Int) : Option[(Int, Int)] =
          if (n == 8)
            Some(2, 3)
        else None  // simplistic - only handles case 2^3
      }

      val PrimePower(p, n) = 8
      p shouldBe 2
      n shouldBe 3
    }
  }
}
