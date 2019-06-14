package com.fdilke.varsymm

import com.fdilke.varsymm.GroupMatcher.groupOf
import org.scalatest.Matchers._
import org.scalatest.{FunSpec, Matchers}

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
      val dihedralGroup: Group[Int] =
        DihedralGroup(4)

      dihedralGroup shouldBe groupOf[Int]
    }

//    it("should have the right order") {
//
//    }
  }
}
