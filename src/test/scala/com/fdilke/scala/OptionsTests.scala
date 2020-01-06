package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._

import org.scalatest.funspec.AnyFunSpec

class OptionsTests extends AnyFunSpec  {
  describe("Options") {
    it("include None") {
      None should not be 'defined
    }
    it("include Some") {
      val two: Option[Int] = Some(2)
      two should be ('defined)
    }
    it("can be fkatmapped") {
      Some(Some(2)).flatten shouldBe Some(2)
    }
    it("can be mapped") {
      Some(2) map { 1 + _ } shouldBe Some(3)
    }
    it("can be foreach'd") {
      None.foreach{ identity }
      var sum = 0
      Some(2).foreach { i =>
        sum = sum + i
      }
      sum shouldBe 2
    }
  }
}
