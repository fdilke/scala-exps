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
      testGroup(3)
      testGroup(27)
      testGroup(343)
    }

    def testGroup(pn: Int) {
      val group = TangentGroup(pn)
      import group._
      elements should have size (pn + 1)
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
