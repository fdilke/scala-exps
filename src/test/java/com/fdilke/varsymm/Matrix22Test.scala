package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class Matrix22Test extends FunSpec {
  describe("2x2 matrices") {
    it("can be compared w.r.t. a tolerance") {
      val matrix = Matrix22(1.2, 2.3, 3.4, 4.5)
      val matrix2 = Matrix22(1.201, 2.302, 3.403, 4.504)

      Matrix22.withinTolerance(matrix, matrix2, 0.002) shouldBe false
      Matrix22.withinTolerance(matrix, matrix2, 0.01) shouldBe true
    }
  }

}
