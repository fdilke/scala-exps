package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import Matchers._
import FelixMatchers._

class FelixMatchersTest extends FunSpec {
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
