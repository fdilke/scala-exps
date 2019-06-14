package com.fdilke.permutations

import org.scalatest.FunSpec
import org.scalatest.{Matchers, FunSpec}
import Matchers._

class PermutationTest extends FunSpec {
  describe("Permutation") {
    it("should have a left and right identity") {
      val id = Permutation.identity
      val r = Permutation(1,2,0)

      r(id) shouldBe r
      id(r) shouldBe r
    }

    it("should act on numbers even if they're out of range") {
      Permutation.identity(0) shouldBe 0
      Permutation.identity(5) shouldBe 5

      val s = Permutation(1,0)
      s(0) shouldBe 1
      s(1) shouldBe 0
      s(2) shouldBe 2
    }

    it("should compose as though acting on the right") {
      val a = Permutation(1,0,3,2)
      val b = Permutation(0,2,1,3)
      val ab = Permutation(1,3,0,2)
      val ba = Permutation(2,0,3,1)

      a(b) shouldBe ab
      b(a) shouldBe ba
      ab should not be ba
    }

    it("should have equality semantics depending only on elements shifted") {

      Permutation(0,1,2) shouldBe Permutation.identity

      val a = Permutation(1,0)
      val b = Permutation(1,0,2)
      val c = Permutation(2,1,0)

      a shouldBe a
      a shouldBe b
      a(a) shouldBe Permutation.identity
      a should not be c

      a.degree shouldBe 2
    }

    it("should get permutations into canonical form") {
      Permutation.essentialDegree(0,1) shouldBe 0
      Permutation.essentialDegree(1,0) shouldBe 2
      Permutation.essentialDegree(1,0,2) shouldBe 2
      Permutation.essentialDegree(1,0,2) shouldBe 2
      Permutation.essentialDegree(1,3,0,2) shouldBe 4

      Permutation.canonical(0,1,2) shouldBe Seq[Int]()
      Permutation.canonical(1,0,2) shouldBe Seq(1,0)
      Permutation.canonical(1,3,0,2) shouldBe Seq(1,3,0,2)
    }

    it("should test parity") {
      Permutation.identity.parity shouldBe 1
      Permutation(1,0).parity shouldBe -1
      Permutation(1,2,0).parity shouldBe 1
      Permutation(2,0,1).parity shouldBe 1
      Permutation(2,0,3,1).parity shouldBe -1
    }
  }
}
