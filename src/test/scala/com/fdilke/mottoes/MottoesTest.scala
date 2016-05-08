package com.fdilke.mottoes

import org.scalatest.Matchers._
import org.scalatest.{FreeSpec, Matchers}
import Sort._

class MottoesTest extends FreeSpec {
  private val Seq(a, h, x, y, z, w, s) = Seq('a, 'h, 'x, 'y, 'z, 'w, 's)

  "The -: operator" - {
    "can be used to build compound sorts" in {
      (x: Sort) shouldBe Sort()(x)
      (x -: y) shouldBe Sort(x)(y)
      x -: (y -: z) shouldBe Sort(x, y)(z)
    }
  }
}
