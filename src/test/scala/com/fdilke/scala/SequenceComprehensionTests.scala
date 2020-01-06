package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._
import org.scalatest.funspec.AnyFunSpec

class SequenceComprehensionTests extends AnyFunSpec {
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
        def withFilter(f: Int => Boolean) = this
      }
      val foo = new Foo
      val bar = for (x <- foo ; if x > 7) yield x
      bar shouldBe ""
    }

    it("can't choose between two map functions!") {
      class Foo {
        def map(f: Int => Int) = ""
        def map(f: String => String) = 7
        def withFilter(f: Int => Boolean) = this
      }
      val bar = for (x : Int <- new Foo) yield x
      bar should be("")

// But can't go:
//      (for (x : String <- new Foo) yield x) should be(7)
    }

    it("can combine inputs from standard classes") {
      val letters = List("a","b")
      val numbers = List(1,2,3)

      val combined = for(l <- letters ; n <- numbers) yield s"$l$n"
      combined should be (List(
        "a1", "a2", "a3", "b1", "b2", "b3"
      ))
    }

    it("can combine inputs with flatMap") {
      class Foo {
        def map(f: Int => Int) = this
        def flatMap(f: Int => Bar) = this
      }
      class Bar {
        def map(f: Int => Int) = this
      }
      val foo = new Foo
      val bar = new Bar

      val combined = for(x <- foo ; y <- bar) yield x+y
      combined should be (foo)
    }

    it("can combine inputs with flatMap and give meaningful results") {
      case class Foo(value: Int) {
        def map(f: Int => Int) = new Foo(f(value))
        def flatMap(g: Int => Bar) = g(value)
      }
      case class Bar(value: Int) {
        def map(f: Int => Int) = new Bar(f(value))
      }
      val foo = Foo(2)
      val bar = Bar(3)

      val combined = for(x <- foo ; y <- bar) yield x+y
      combined should be (Bar(5))
    }

    it("can combine inputs to build an algebraic operator") {
      class Context[T] {
        def map[V](f: T => V): (Seq[Any]) => V = {
          case s: Seq[T] => f(s.head)
        }
        def flatMap[V](g: T => Seq[Any] => V): (Seq[Any]) => V =
          s => g(s.head.asInstanceOf[T])(s.tail)
      }

      val operator = for (x <- new Context[Int]) yield (x*x)
      operator(Seq(3)) shouldBe 9

      val operator2 =
        for (x <- new Context[Int] ; y <- new Context[String])
          yield ( x + y )
      operator2(Seq(2, "baz")) shouldBe "2baz"
    }
  }
}
