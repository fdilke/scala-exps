package com.fdilke.permutations

import org.scalatest.{Matchers, FunSpec}
import Matchers._

class GenerateGroupTest extends FunSpec {
  describe("Generating groups") {
    it("should work even with the empty set as a basis") {
      GenerateGroup().set should be (
        Set(OldPermutation.identity)
      )
    }

    it("should give cyclic groups when started with a single element") {
      val id = OldPermutation(0,1,2,3)
      val a = OldPermutation(1,0,2,3)
      GenerateGroup(a).set should be (Set(id, a))
    }

    it("should give the Klein four-group when started with appropriate generators") {
      val id = OldPermutation(0,1,2,3)
      val a = OldPermutation(1,0,2,3)
      val b = OldPermutation(0,1,3,2)
      GenerateGroup(a, b).set should be (
        Set(id, a, b, a(b))
      )
    }

    it("should give the dihedral group of order 18 on two generators") {
      val a = OldPermutation(1,2,3,4,5,6,7,8,0)
      val b = OldPermutation(0,8,7,6,5,4,3,2,1)
      GenerateGroup(a, b).set.size should be (18)
    }
  }
}