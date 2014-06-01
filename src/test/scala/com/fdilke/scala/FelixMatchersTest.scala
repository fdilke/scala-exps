package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import Matchers._
import FelixMatchers._

class FelixMatchersTest extends FunSpec {
  describe("The anInstanceOf matcher") {
    it("should match objects of specified type") {
      "" shouldBe anInstanceOf[String]
// and ideally:
//      Array(2) shouldBe ( anInstanceOf[Array[Int]] )
//      3 shouldBe ( anInstanceOf[Int] )
    }
  }
}
