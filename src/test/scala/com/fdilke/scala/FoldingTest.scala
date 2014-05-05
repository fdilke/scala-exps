package com.fdilke.scala

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import ShouldMatchers._

// User: Felix Date: 05/05/2014 Time: 18:58

class FoldingTest extends FunSpec {
  describe("folding") {
    it("works from the left (and by default)") {
      Seq(1,2,4).fold(0)((x, y) => x - y) shouldBe -7

      Seq(1,2,4).foldLeft(0)((x, y) => x - y) shouldBe -7
      Seq(1,2,4)./:(0)((x, y) => x - y) shouldBe -7
      0 - 1 - 2 - 4 shouldBe -7
    }
    it("works from the right") {
      Seq(1,2,4).foldRight(0)((x, y) => x - y) shouldBe 3
      Seq(1,2,4).:\(0)((x, y) => x - y) shouldBe 3
      1 - (2 - (4 - 0)) shouldBe 3
    }
  }
}
