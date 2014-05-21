package com.fdilke.util

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import ShouldMatchers._
import com.fdilke.util.FancyMultiplicationImplicits._

class FancyMultiplicationTest extends FunSpec {
  describe("The fancy multiplication") {
    it("gives the values we expect") {
      0 ** 3 shouldBe 0
      2 ** 3 shouldBe 6
      3 ** 3 shouldBe 5
      3 ** 5 shouldBe 15
    }

    it("has 0 as a zero") {
      for(i <- 0 to 10) {
          (0 ** i) shouldBe 0
      }
    }

    it("has 1 as an identity") {
      for(i <- 0 to 10) {
          (1 ** i) shouldBe i
      }
    }

    it("is increasing for multipliers > 1") {
      for(i <- 2 to 10;
          j <- 1 to 10) {
        (i ** j) should be > j
      }
    }

    it("is commutative") {
      for(i <- 0 to 10;
          j <- 0 to 10) {
          (i ** j) shouldBe (j ** i)
      }
    }

    it("is associative") {
      for(i <- 0 to 10;
          j <- 0 to 10;
          k <- 0 to 10) {
          ((i ** j) ** k) shouldBe (i ** (j ** k))
      }
    }

    it("distributes over ^") {
      for(i <- 0 to 10;
          j <- 0 to 10;
          k <- 0 to 10) {
          (i ** (j ^ k)) shouldBe ((i ** j) ^ (i ** k))
      }
    }
  }
}