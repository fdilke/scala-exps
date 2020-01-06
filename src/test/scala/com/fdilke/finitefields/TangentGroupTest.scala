package com.fdilke.finitefields

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class TangentGroupTest extends AnyFunSpec {
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

    def testGroup(q: Int) : Unit = {
      println(s"testing GF($q)")
      val group = TangentGroup(q)
      import group.zero
      group should have size (q + 1)
      for { a <- group } {
        zero + a shouldBe a
        -a + a shouldBe zero
        for { b <- group } {
          a + b shouldBe (b + a)
          for { c <- group } {
            (a + b) + c shouldBe ( a + (b + c) )
          }
        }
      }
    }
  }

  it("can be expressed as ratios") {
    val group = TangentGroup(7)
    val field = group.field

    for {
      a <- field
    }
      GroupElement(group, Some(a)).asRatio shouldBe
        Ratio(a, field.I)

    group.infinity.asRatio shouldBe
      Ratio(field.I, field.O)

    for {
      a <- group
    }
      group.fromRatio(a.asRatio) shouldBe a
  }
}
