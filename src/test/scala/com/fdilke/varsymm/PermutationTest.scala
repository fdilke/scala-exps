package com.fdilke.varsymm

import org.scalatest.matchers.should.Matchers._
import org.scalatest.funspec.AnyFunSpec

class PermutationTest extends AnyFunSpec {
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

    it("can be inverted") {
      Permutation(1,0,2).inverse shouldBe
        Permutation(1,0,2)

      Permutation(1,2,0).inverse shouldBe
        Permutation(2,0,1)
    }

    it("of a given degree can be enumerated") {
      Permutation.enumerate(degree = 1) shouldBe Set(
        Permutation.identity(1)
      )

      Permutation.enumerate(degree = 3) shouldBe Set(
        Permutation(0, 1, 2), Permutation(0, 2, 1),
        Permutation(1, 0, 2), Permutation(1, 2, 0),
        Permutation(2, 0, 1), Permutation(2, 1, 0)
      )
    }

    it("of a given degree can be made into a group") {
      val group: Group[Permutation] =
        Permutation.group(degree = 4)

      group should GroupMatcher.beAGroupOf[Permutation]
      group.order shouldBe 24
    }
  }
}
