package com.fdilke.varsymm

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class DihedralSymmetryTest extends AnyFunSpec {
  private val TOLERANCE = 0.001

  describe("Dihedral symmetries") {
    it("can be straight rotations, composing by addition") {
      val ds1 = DihedralSymmetry(20, reflect=false, 3)
      val ds2 = DihedralSymmetry(20, reflect=false, 5)
      ds1 * ds2 shouldBe DihedralSymmetry(20, false, 8)
    }

    it("can be straight rotations, composing by addition, using modular arithmetic") {
      val ds1 = DihedralSymmetry(6, reflect=false, 3)
      val ds2 = DihedralSymmetry(6, reflect=false, 5)
      ds1 * ds2 shouldBe DihedralSymmetry(6, false, 2)
    }

    it("can be straight reflections, whose squqre is the identity") {
      val ds = DihedralSymmetry(6, reflect=true, 0)
      ds * ds shouldBe DihedralSymmetry.unit(6)
    }

    it("can be straight reflections, which composed with any other, flip its bit, depending on order") {
      val ds = DihedralSymmetry(8, reflect=true, 0)
      val ds2 = DihedralSymmetry(8, reflect=false, 7)
      ds * ds2 shouldBe DihedralSymmetry(8, reflect=true, 7)
      ds2 * ds shouldBe DihedralSymmetry(8, reflect=true, 1)
    }

    it("can be mixed, requiring all these interactions combined") {
      val ds = DihedralSymmetry(8, reflect=true, 3)
      val ds2 = DihedralSymmetry(8, reflect=true, 4)
      ds * ds2 shouldBe DihedralSymmetry(8, reflect=false, 1)
    }

    it("of differing moduli cannot be composed") {
      val ds = DihedralSymmetry(7, reflect=true, 3)
      val ds2 = DihedralSymmetry(8, reflect=true, 4)
      intercept[IllegalArgumentException] {
        ds * ds2
      }
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

    it("works as expected for the fundamental reflection") {
      Matrix22.withinTolerance(
        DihedralSymmetry(7, reflect=true, 0).toMatrix,
        Matrix22(1,0,0,-1),
        TOLERANCE
      ) shouldBe true
    }

    it("works as expected for the fundamental rotation") {
      val c = 1/Math.sqrt(2)
      val s = c
      Matrix22.withinTolerance(
        DihedralSymmetry(8, reflect=false, 1).toMatrix,
        Matrix22(c, -s, s, c),
        TOLERANCE
      ) shouldBe true
    }

    it("works as expected for a typical hybrid symmetry") {
      val s = 1/Math.sqrt(2)
      val c = -s
      Matrix22.withinTolerance(
        DihedralSymmetry(8, reflect=true, 3).toMatrix,
        Matrix22(c, -s, -s, -c),
        TOLERANCE
      ) shouldBe true
    }

    it("is homomorphic") {
      val x = DihedralSymmetry(9, reflect=true, 4)
      val y = DihedralSymmetry(9, reflect=false, 7)
      Matrix22.withinTolerance(
        x.toMatrix * y.toMatrix,
        (x * y).toMatrix,
        TOLERANCE
      ) shouldBe true
    }

    it("is injective") {
      val symmetries: Set[DihedralSymmetry] =
        DihedralGroup(14).elements
      for {
        x <- symmetries
        y <- symmetries
      }
        if (x != y)
          Matrix22.withinTolerance(
            x.toMatrix,
            y.toMatrix,
            TOLERANCE
          ) shouldBe false
    }
  }
}
