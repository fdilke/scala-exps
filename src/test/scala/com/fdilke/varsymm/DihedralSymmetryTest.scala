package com.fdilke.varsymm

import org.scalatest.Matchers._
import org.scalatest.{FunSpec, Matchers}

class DihedralSymmetryTest extends FunSpec {
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

}
