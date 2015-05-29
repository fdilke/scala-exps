package com.fdilke.scala

import org.scalatest.FunSpec
import org.scalatest.Matchers._

case class Foo(name: String)

object FooHelpers {
  implicit class FooHelper(val sc: StringContext) extends AnyVal {
    def foo(args: Any*): Foo = Foo(sc.parts.mkString)
  }
}

class StringInterpolationTests extends FunSpec {

  describe("String interpolation") {
    it("can use direct substitution with expressions") {
      val message = "Hello"
      val number = 7
      s"Message is $message, number is $number, expression is ${number * 3}" should be(
        "Message is Hello, number is 7, expression is 21"
      )
    }

    it("can do formatting") {
      val number = 3.14159
      f"formatted value is $number%2.2f" should be (
        "formatted value is 3.14"
      )
    }

    it("can generate raw strings, ignoring escape sequences") {
      raw"Hello\n\007\b".toCharArray should be (
        Array('H','e','l','l','o','\\','n','\\','0','0','7','\\','b')
      )
    }

    it("can work with user-defined prefixes") {
      import com.fdilke.scala.FooHelpers._
      val myFoo = foo"Felix"
      assert(myFoo.isInstanceOf[Foo])
      myFoo.name should be("Felix")
    }
  }
}
