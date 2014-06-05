package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import Matchers._
import scala.collection.mutable

class TraversableTests extends FunSpec {
  describe("Traversables") {
    it("are a way to view sets") {
      val foo: Traversable[String] = Set("a" ,"b", "c")

      val collect = scala.collection.mutable.Set[String]()
      foo.foreach( collect += _ )
      collect shouldBe foo
    }

    it("- it's easy to roll your own") {
      val foo = new Traversable[String]() {
        override def foreach[U](f: String => U) = {
          f("Hello")
          f("Goodbye")
        }
      }
      foo.toSet shouldBe Set("Hello", "Goodbye")
    }

    it("come with lots of added value") {
      val foo = new Traversable[Int]() {
        override def foreach[U](f: Int => U) = {
          f(7)
          f(9)
        }
      }

      foo.toList shouldBe List(7, 9)
      foo ++ foo shouldBe List(7, 9, 7, 9)
      foo./:(3)(_ * _) shouldBe 189
      foo.filter(_ > 8) shouldBe List(9)
    }

    it("don't traverse any more than they have to") {
      val foo = new Traversable[Int]() {
        override def foreach[U](f: Int => U) = {
          f(1)
          f(4)
          f(9)
        }
      }
      val received = mutable.MutableList[Int]()
      foo.exists { p =>
        received += p
        p > 3
      } shouldBe true
      received shouldBe Seq(1, 4)
    }
  }
}
