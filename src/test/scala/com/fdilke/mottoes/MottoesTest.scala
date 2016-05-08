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
  }
}
