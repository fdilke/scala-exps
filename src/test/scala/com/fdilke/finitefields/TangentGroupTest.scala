package com.fdilke.finitefields

import org.scalatest.{Matchers, FunSpec}
import Matchers._

class TangentGroupTest extends FunSpec {
  describe("The tangent group") {
    it("can only be computed in the evenly odd case") {
      intercept[IllegalArgumentException] {
        testGroup(5)
      }
    }

    it("is a group") {
      Seq(
        3, 7, 11, 27 // 343, 1331 take too long
      ) foreach
        testGroup
    }

    def testGroup(q: Int) {
      println(s"testing GF($q)")
      val group = TangentGroup(q)
      import group.{ elements, zero }
      elements should have size (q + 1)
      elements.foreach { a =>
        zero + a shouldBe a
        -a + a shouldBe zero
        elements.foreach { b =>
          a + b shouldBe (b + a)
          elements.foreach { c =>
            (a + b) + c shouldBe ( a + (b + c) )
          }
        }
      }
    }
  }
}
