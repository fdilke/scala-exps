package com.fdilke.varsymm

import com.fdilke.varsymm.GroupMatcher.beAGroupOf
import org.scalatest.FunSpec
import org.scalatest.Matchers._

class DihedralGroupTest extends FunSpec {

  describe("The dihedral group") {
    it("makes sense only for a positive number of elements") {
      intercept[IllegalArgumentException] {
        DihedralGroup(-2)
      }
      intercept[IllegalArgumentException] {
        DihedralGroup(0)
      }
    }

    it("makes sense only for an even number of elements") {
      intercept[IllegalArgumentException] {
        DihedralGroup(7)
      }
    }

    it("should actually be a group") {
      DihedralGroup(4) should beAGroupOf[DihedralSymmetry]
    }

    it("should have the right order") {
      DihedralGroup(8).order shouldBe 8
    }
  }
}
