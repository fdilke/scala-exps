package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class Matrix22Test extends FunSpec {
  private val TOLERANCE = 0.001

  describe("2x2 matrices") {
    it("can be compared w.r.t. a tolerance") {
      val matrix = Matrix22(1.2, 2.3, 3.4, 4.5)
      val matrix2 = Matrix22(1.201, 2.302, 3.403, 4.504)

      Matrix22.withinTolerance(matrix, matrix2, 0.002) shouldBe false
      Matrix22.withinTolerance(matrix, matrix2, 0.01) shouldBe true
    }

    it("have a similar utility method for comparing vectors") {
      val vector: (Double, Double) = (1.2, 2.3)
      val vector2: (Double, Double) = (1.201, 2.302)

      Matrix22.withinTolerance(vector, vector2, 0.001) shouldBe false
      Matrix22.withinTolerance(vector, vector2, 0.01) shouldBe true
    }

    it("can be multiplied") {
      val a = Matrix22(
        2,  3,
        4,  5
      )
      val b = Matrix22(
        6,  7,
        8,  9
      )
      Matrix22.withinTolerance(
        a * b,
        Matrix22(
          36, 41,
          64, 73
        ),
        TOLERANCE
      ) shouldBe true
    }

    it("can embody rotations") {
      val c = 0.5
      val s = Math.sqrt(3)/2
      Matrix22.withinTolerance(
        Matrix22.rotation(
          Math.PI / 3
        ),
        Matrix22(
          c, -s,
          s, c
        ),
        TOLERANCE
      ) shouldBe true
    }

    it("can embody rotations homomorphically") {
      Matrix22.withinTolerance(
        Matrix22.rotation(
          0
        ),
        Matrix22.identity,
        TOLERANCE
      ) shouldBe true

      val angle1 = Math.PI / 4
      val angle2 = Math.PI / 5
      Matrix22.withinTolerance(
        Matrix22.rotation(
          angle1
        ) * Matrix22.rotation(
          angle2
        ),
        Matrix22.rotation(
          angle1 + angle2
        ),
        TOLERANCE
      ) shouldBe true
    }

    it("can act on vectors on the right") {
      Matrix22.withinTolerance(
        (1.0, 2.0) *: Matrix22(
          3, 4,
          5, 6
        ),
        ( 13.0, 16.0 ),
        TOLERANCE
      ) shouldBe true
    }
  }
}
