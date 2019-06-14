package com.fdilke.permutations

import org.scalatest.FunSpec
import org.scalatest.{Matchers, FunSpec}
import Matchers._

class OldPermutationTest extends FunSpec {
  describe("Permutation") {
    it("should have a left and right identity") {
      val id = OldPermutation.identity
      val r = OldPermutation(1,2,0)

      r(id) shouldBe r
      id(r) shouldBe r
    }

    it("should act on numbers even if they're out of range") {
      OldPermutation.identity(0) shouldBe 0
      OldPermutation.identity(5) shouldBe 5

      val s = OldPermutation(1,0)
      s(0) shouldBe 1
      s(1) shouldBe 0
      s(2) shouldBe 2
    }

    it("should compose as though acting on the right") {
      val a = OldPermutation(1,0,3,2)
      val b = OldPermutation(0,2,1,3)
      val ab = OldPermutation(1,3,0,2)
      val ba = OldPermutation(2,0,3,1)

      a(b) shouldBe ab
      b(a) shouldBe ba
      ab should not be ba
    }

    it("should have equality semantics depending only on elements shifted") {

      OldPermutation(0,1,2) shouldBe OldPermutation.identity

      val a = OldPermutation(1,0)
      val b = OldPermutation(1,0,2)
      val c = OldPermutation(2,1,0)

      a shouldBe a
      a shouldBe b
      a(a) shouldBe OldPermutation.identity
      a should not be c

      a.degree shouldBe 2
    }

    it("should get permutations into canonical form") {
      OldPermutation.essentialDegree(0,1) shouldBe 0
      OldPermutation.essentialDegree(1,0) shouldBe 2
      OldPermutation.essentialDegree(1,0,2) shouldBe 2
      OldPermutation.essentialDegree(1,0,2) shouldBe 2
      OldPermutation.essentialDegree(1,3,0,2) shouldBe 4

      OldPermutation.canonical(0,1,2) shouldBe Seq[Int]()
      OldPermutation.canonical(1,0,2) shouldBe Seq(1,0)
      OldPermutation.canonical(1,3,0,2) shouldBe Seq(1,3,0,2)
    }

    it("should test parity") {
      OldPermutation.identity.parity shouldBe 1
      OldPermutation(1,0).parity shouldBe -1
      OldPermutation(1,2,0).parity shouldBe 1
      OldPermutation(2,0,1).parity shouldBe 1
      OldPermutation(2,0,3,1).parity shouldBe -1
    }
  }
}
