package com.fdilke.mottoes

import org.scalatest.Matchers._
import org.scalatest.{FreeSpec, Matchers}
import Sort._
import Expression._

class MottoesTest extends FreeSpec {
  private val Seq(a, h, x, y, z, w, s) = Seq('a, 'h, 'x, 'y, 'z, 'w, 's)

  "The -: operator" - {
    "can be used to build compound sorts" in {
      (x: Sort) shouldBe λ()(x)
      (x -: y) shouldBe λ(x)(y)
      x -: y -: z shouldBe λ(x, y)(z)
    }

    "has sane equality semantics" in {
      (x: Expression) should be (x: Expression)
      (x: Expression) should not be (y : Expression)
      x should not be (x -: y)
      (x: Expression) should not be (x -: y: Expression)
    }

    "is not idempotent" in {
      x should not be (x -: x)
    }

    "is not symmetric" in {
      (x -: y) should not be (y -: x)
    }

    "associates to the right and NOT the left" in {
      (x -: (y -: z)) should not be ((x -: y) -: z)
      (x -: y -: z) shouldBe (x -: (y -: z))
    }
  }

  // TODO: continue conversion of tests from old motto code
}
