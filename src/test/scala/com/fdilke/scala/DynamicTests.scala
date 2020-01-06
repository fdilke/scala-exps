package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

import scala.language.dynamics

class DynamicTests extends AnyFunSpec {
  describe("Dynamic invocation") {
    it("can be used for selecting a field") {
      class Foo extends Dynamic {
        def selectDynamic(name: String) =
          "invoked " + name
      }
      val foo = new Foo
      foo.bar shouldBe "invoked bar"
    }

    it("can be used for invoking a method") {
      class Foo extends Dynamic {
        def applyDynamic(name: String)(n: Int) =
          name match {
            case "inc" => n + 1
            case "dec" => n - 1
          }
      }
      val foo = new Foo
      foo.inc(2) shouldBe 3
      foo.dec(2) shouldBe 1
    }

    it("can be used for invoking a method with custom arg and return types") {
      class Foo extends Dynamic {
        def applyDynamic(name: String)(bar: Bar): Baz =
          name match {
            case "doIt" => Baz(-1)
            case _ => ???
          }
      }
      class Bar
      case class Baz(n: Int)
      val foo = new Foo
      val bar = new Bar
      foo.doIt(bar) shouldBe Baz(-1)
      intercept[NotImplementedError] {
        foo.other(bar)
      }
    }
  }
}
