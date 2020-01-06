package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class FunctionComposition extends AnyFunSpec {
  describe("Function can be composed") {

    def f(n: Int): Boolean = n < 3

    def g(b: Boolean): String = b.toString

    it("using compose, categorystyle") {
      (g _ compose f)(4) shouldBe "false"
    }

    it("using andThen, reverse categorystyle") {
      (f _ andThen g)(2) shouldBe "true"
    }
  }
}
