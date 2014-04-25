package com.fdilke.scala

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import ShouldMatchers._

// User: Felix Date: 23/04/2014 Time: 21:20

class MonadicForTests extends FunSpec {
  describe("The built-in monadic logic of 'for'") {
    it("should express 'map' implicitly") {
      class Foo {
        def map(f: Int => Int) : Foo = this
      }
      val foo = new Foo
      val bar = for (x <- foo) yield x
      bar shouldBe foo
    }

    it("can adapt to map returning different types") {
      class Foo {
        def map(f: Int => Int) = ""
      }
      val foo = new Foo
      val bar = for (x <- foo) yield x
      bar shouldBe ""
    }

    it("can apply filters") {
      class Foo {
        def map(f: Int => Int) = ""
        def filter(f: Int => Boolean) = this
      }
      val foo = new Foo
      val bar = for (x <- foo ; if x > 7) yield x
      bar shouldBe ""
    }
  }
}
