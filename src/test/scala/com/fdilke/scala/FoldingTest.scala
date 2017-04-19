package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import Matchers._

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
    it("...left fold spelt out in full") {
      def f(
        s: String,
        i: Int
      ): String =
        i + s
      var (a0, a1) = (1, 2)
      var b = "B"
      Seq(a0, a1).foldLeft(b)(f) shouldBe f(f(b, a0), a1)
    }
    it("...right fold spelt out in full") {
      def f(
        i: Int,
        s: String
      ): String =
        i + s
      var (a0, a1) = (1, 2)
      var b = "B"
      Seq(a0, a1).foldRight(b)(f) shouldBe f(a0, f(a1, b))
    }
    it("... variation on right fold") {
      type S = Int

      class FoldingTarget {
        def absorb(s: S): FoldingTarget =
          new FoldingTarget
      }

      val xx: Traversable[S] =
        Traversable(1,2,3)

      xx.foldLeft(
        new FoldingTarget
      ) { (x: FoldingTarget, y: S) =>
        x.absorb(y)
      }
    }
  }
}
