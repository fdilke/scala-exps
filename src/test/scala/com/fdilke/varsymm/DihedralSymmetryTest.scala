package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class DihedralSymmetryTest extends FunSpec {
  private val TOLERANCE = 0.001

  describe("Dihedral symmetries") {
    it("can be straight rotations, composing by addition") {
      val ds1 = DihedralSymmetry(reflect=false, 3)
      val ds2 = DihedralSymmetry(reflect=false, 5)
      ds1.compose(ds2, 20) shouldBe DihedralSymmetry(false, 8)
    }

    it("can be straight rotations, composing by addition, using modular arithmetic") {
      val ds1 = DihedralSymmetry(reflect=false, 3)
      val ds2 = DihedralSymmetry(reflect=false, 5)
      ds1.compose(ds2, 6) shouldBe DihedralSymmetry(false, 2)
    }

    it("can be straight reflections, whose squqre is the identity") {
      val ds = DihedralSymmetry(reflect=true, 0)
      ds.compose(ds, 6) shouldBe DihedralSymmetry.unit
    }

    it("can be straight reflections, which composed with any other, flip its bit, depending on order") {
      val ds = DihedralSymmetry(reflect=true, 0)
      val ds2 = DihedralSymmetry(reflect=false, 7)
      ds.compose(ds2, 8) shouldBe DihedralSymmetry(reflect=true, 7)
      ds2.compose(ds, 8) shouldBe DihedralSymmetry(reflect=true, 1)
    }

    it("can be mixed, requiring all these interactions combined") {
      val ds = DihedralSymmetry(reflect=true, 3)
      val ds2 = DihedralSymmetry(reflect=true, 4)
      ds.compose(ds2, 8) shouldBe DihedralSymmetry(reflect=false, 1)
    }
  }

  describe("Inversion of dihedral symmetries") {
    it("works as expected for the identity") {
      DihedralSymmetry.unit.invert(2) shouldBe DihedralSymmetry.unit
    }

    it("works as expected for pure rotations") {
      val ds = DihedralSymmetry(reflect=false, 3)
      ds.invert(8) shouldBe DihedralSymmetry(reflect=false, 5)
    }

    it("works as expected for other elements") {
      val ds = DihedralSymmetry(reflect=true, 3)
      ds.invert(8) shouldBe ds
    }
  }

  describe("Matrix representation of dihedral symmetries") {
    it("works as expected for the identity") {
      Matrix22.withinTolerance(
        DihedralSymmetry.unit.toMatrix,
        Matrix22.identity,
        TOLERANCE
      ) shouldBe true
    }

//    it("works as expected for the fundamental reflection") {
//      Matrix22.withinTolerance(
//        DihedralSymmetry(true, 0).toMatrix,
//        Matrix22(1,0,0,-1),
//        TOLERANCE
//      ) shouldBe true
//    }
//
//    it("works as expected for the fundamental rotation") {
//      Matrix22.withinTolerance(
//        DihedralSymmetry(true, 0).toMatrix,
//        Matrix22(1,0,0,-1),
//        TOLERANCE
//      ) shouldBe true
//    }
  }
}
