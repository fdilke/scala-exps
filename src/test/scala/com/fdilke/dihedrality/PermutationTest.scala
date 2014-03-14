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

    it("should compose as though acting on the right") {
      val a = Permutation(1,0,3,2)
      val b = Permutation(0,2,1,3)
      val ab = Permutation(1,3,0,2)

      a(b) should be (ab)
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
