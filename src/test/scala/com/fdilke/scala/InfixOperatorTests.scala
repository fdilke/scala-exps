package com.fdilke.scala

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers._

// User: Felix Date: 05/05/2014 Time: 18:45

class InfixOperatorTests extends FunSpec {
  describe("Infix operators") {
    it("left associate") {
      case class Foo(value: Int) {
        def x(that: Foo) : Foo = Foo(value - that.value)
      }
      val a = new Foo(1)
      val b = new Foo(2)
      val c = new Foo(4)
      (a x b x c) shouldBe Foo(-5)
    }
  }
}