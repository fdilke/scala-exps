package com.fdilke.dihedrality

import org.scalatest.{ShouldMatchers, FunSpec}
import org.scalatest.Assertions._

class PermutationTest extends FunSpec with ShouldMatchers {
  describe("Permutation") {
    it("should have a left and right identity") {
//      val id = new Permutation
      val id = Permutation.identity(3)
      val r = Permutation(1,2,0)

      r(id) should be (r)
      id(r) should be (r)
    }

    it("should act on numbers even if they're out of range") {
      val s = Permutation(1,0)
      s(0) should be(1)
      s(1) should be(0)
      s(2) should be(2)
    }

    it("should compose as though acting on the right") {
      val a = Permutation(1,0,3,2)
      val b = Permutation(0,2,1,3)
      val ab = Permutation(1,3,0,2)
      val ba = Permutation(2,0,3,1)

      a(b) should be (ab)
      b(a) should be (ba)
      ab should not be ba
    }

    it("should have equality semantics depending only on elements shifted") {
      val a = Permutation(1,0)
      val b = Permutation(1,0,2)
      val c = Permutation(2,1,0)

      a should be(a)
      a should be(b)
      a should not be c

      a.degree should be(2)
    }

    it("should get permutations into canonical form") {
      Permutation.essentialDegree(0,1) should be(0)
      Permutation.essentialDegree(1,0) should be(2)
      Permutation.essentialDegree(1,0,2) should be(2)
      Permutation.essentialDegree(1,0,2) should be(2)
      Permutation.essentialDegree(1,3,0,2) should be(4)

      Permutation.canonical(0,1,2) should be(Seq[Int]())
      Permutation.canonical(1,0,2) should be(Seq(1,0))
      Permutation.canonical(1,3,0,2) should be(Seq(1,3,0,2))
    }

    it("should test parity") {
      Permutation.identity(3).parity should be (1)
      Permutation(1,0).parity should be (-1)
      Permutation(1,2,0).parity should be (1)
      Permutation(2,0,1).parity should be (1)
      Permutation(2,0,3,1).parity should be (-1)
    }
  }
}
