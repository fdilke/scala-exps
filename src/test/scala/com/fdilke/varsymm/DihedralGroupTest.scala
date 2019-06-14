package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.{Matchers, FunSpec}
import Matchers._

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

//    it("should actually be a group") {
//      DihedralGroup()
//    }

//    it("should have the right order") {
//
//    }
  }
}
