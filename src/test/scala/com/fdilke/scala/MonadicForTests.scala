package com.fdilke.scala

import org.scalatest.{ShouldMatchers, FunSpec}
import org.junit.Assert
import Assert._

// User: Felix Date: 23/04/2014 Time: 21:20

class MonadicForTests extends FunSpec with ShouldMatchers {
  describe("The built-in monadic logic of 'for'") {
    it("should express 'map' implicitly") {
      class Foo {
        def map(f: Int => Int) : Foo = this
      }
      val foo = new Foo
      val bar = for (x <- foo) yield x
      assertTrue(bar.isInstanceOf[Foo])
    }

    it("can adapt to map returning different types") {
      class Foo {
        def map(f: Int => Int) = ""
      }
      val foo = new Foo
      val bar = for (x <- foo) yield x
      assertTrue(bar.isInstanceOf[String])
    }

    it("can apply filters") {
      class Foo {
        def map(f: Int => Int) = ""
        def filter(f: Int => Boolean) = this
      }
      val foo = new Foo
      val bar = for (x <- foo ; if x > 7) yield x
      assertTrue(bar.isInstanceOf[String])
    }
  }
}
