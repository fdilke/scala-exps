package com.fdilke.dihedrality

import org.scalatest.{ShouldMatchers, FunSpec}

class GroupTest extends FunSpec with ShouldMatchers {
  describe("Generating groups") {
    it("should work even with the empty set as a basis") {
      Group.generate().set should be (
        Set(Permutation.identity)
      )
    }

    it("should give cyclic groups when started with a single element") {
      val id = Permutation(0,1,2,3)
      val a = Permutation(1,0,2,3)
      Group.generate(a).set should be (Set(id, a))
    }

    it("should give the Klein four-group when started with appropriate generators") {
      val id = Permutation(0,1,2,3)
      val a = Permutation(1,0,2,3)
      val b = Permutation(0,1,3,2)
      Group.generate(a, b).set should be (
        Set(id, a, b, a(b))
      )
    }

    it("should give the dihedral group of order 18 on two generators") {
      val a = Permutation(1,2,3,4,5,6,7,8,0)
      val b = Permutation(0,8,7,6,5,4,3,2,1)
      Group.generate(a, b).set.size should be (18)
    }
  }
}