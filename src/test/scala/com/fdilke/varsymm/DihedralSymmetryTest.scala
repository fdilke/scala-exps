package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class DihedralSymmetryTest extends FunSpec {
  private val TOLERANCE = 0.001

  describe("Dihedral symmetries") {
    it("can be straight rotations, composing by addition") {
      val ds1 = DihedralSymmetry(20, reflect=false, 3)
      val ds2 = DihedralSymmetry(20, reflect=false, 5)
      ds1.compose(ds2) shouldBe DihedralSymmetry(20, false, 8)
    }

    it("can be straight rotations, composing by addition, using modular arithmetic") {
      val ds1 = DihedralSymmetry(6, reflect=false, 3)
      val ds2 = DihedralSymmetry(6, reflect=false, 5)
      ds1.compose(ds2) shouldBe DihedralSymmetry(6, false, 2)
    }

    it("can be straight reflections, whose squqre is the identity") {
      val ds = DihedralSymmetry(6, reflect=true, 0)
      ds.compose(ds) shouldBe DihedralSymmetry.unit(6)
    }

    it("can be straight reflections, which composed with any other, flip its bit, depending on order") {
      val ds = DihedralSymmetry(8, reflect=true, 0)
      val ds2 = DihedralSymmetry(8, reflect=false, 7)
      ds.compose(ds2) shouldBe DihedralSymmetry(8, reflect=true, 7)
      ds2.compose(ds) shouldBe DihedralSymmetry(8, reflect=true, 1)
    }

    it("can be mixed, requiring all these interactions combined") {
      val ds = DihedralSymmetry(8, reflect=true, 3)
      val ds2 = DihedralSymmetry(8, reflect=true, 4)
      ds.compose(ds2) shouldBe DihedralSymmetry(8, reflect=false, 1)
    }
  }

  describe("Inversion of dihedral symmetries") {
    it("works as expected for the identity") {
      DihedralSymmetry.unit(2).invert shouldBe DihedralSymmetry.unit(2)
    }

    it("works as expected for pure rotations") {
      val ds = DihedralSymmetry(8, reflect=false, 3)
      ds.invert shouldBe DihedralSymmetry(8, reflect=false, 5)
    }

    it("works as expected for other elements") {
      val ds = DihedralSymmetry(8, reflect=true, 3)
      ds.invert shouldBe ds
    }
  }

  describe("Matrix representation of dihedral symmetries") {
    it("works as expected for the identity") {
      Matrix22.withinTolerance(
        DihedralSymmetry.unit(2).toMatrix,
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
