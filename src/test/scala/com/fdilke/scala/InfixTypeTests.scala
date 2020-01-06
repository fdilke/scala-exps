package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._
import org.scalatest.funspec.AnyFunSpec

class InfixTypeTests extends AnyFunSpec {

  type →[T, U] = T => U
  type →:[T, U] = T → U
  type ~:[A,B] = Map[A,B]
  type ~~~:[A,B] = (A,B)

  describe("Infix types") {
    it("enable a funky arrow notation") {
      def fun(n: Int) = n.toString
      val arrow: Int → String = fun
      def funWithArrows(arrow: Int → String) = arrow(3)
      funWithArrows(arrow) shouldBe "3"
    }
    it("left-associate") {
      val notACurriedArrow: Int → Boolean → String = {
        i2b =>
          if(i2b(2))
            "Likes 2"
          else
            "Rejects 2"
      }
      notACurriedArrow(_ > 1) shouldBe "Likes 2"
      notACurriedArrow(_ % 4 == 1) shouldBe "Rejects 2"
    }
    it("can also be made to right-associate") {
      val curriedArrow: Int →: Boolean →: String =
        i => b =>
          if(b)
            i.toString()
          else
            "Not " + i.toString()

      curriedArrow(1)(true) shouldBe "1"
      curriedArrow(3)(false) shouldBe "Not 3"
    }
  }
}

