package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class PermutationTest extends FunSpec {
  describe("Permutations") {
    it("of a given degree can be created and composed") {
      val p1 = Permutation(1,0,2)
      val p2 = Permutation(0,2,1)
      p1.degree shouldBe 3
      p1(1) shouldBe 0
      p1(p2) shouldBe Permutation(2,0,1)
    }

    it("of different degrees can't be combined") {
      intercept[IllegalArgumentException] {
        val p1 = Permutation(1, 0)
        val p2 = Permutation(0, 2, 1)
        p1(p2)
      }
    }

    it("equivalent to the identity can be created for any degree") {
      Permutation.identity(4) shouldBe Permutation(0, 1, 2, 3)
    }

    it("of a given degree can be enumerated") {
      Permutation.enumerate(degree = 1) shouldBe Seq(
        Permutation.identity(1)
      )

      Permutation.enumerate(degree = 3).toSet shouldBe Set(
        Permutation(0, 1, 2), Permutation(0, 2, 1),
        Permutation(1, 0, 2), Permutation(1, 2, 0),
        Permutation(2, 0, 1), Permutation(2, 1, 0)
      )
    }
  }
}
