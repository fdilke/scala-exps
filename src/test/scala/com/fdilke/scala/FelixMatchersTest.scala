package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._
import FelixMatchers._
import org.scalatest.funspec.AnyFunSpec

class FelixMatchersTest extends AnyFunSpec {
  describe("The anInstanceOf matcher") {
    it("should match simple objects of specified type") {
      "" shouldBe anInstanceOf[String]
    }

    it("should match fancier objects of specified type") {
      "" shouldBe ofType[String]
      Array(2) shouldBe ofType[Array[Int]]
// and ideally...
//      3 shouldBe ofType[Int]
    }
  }
}
