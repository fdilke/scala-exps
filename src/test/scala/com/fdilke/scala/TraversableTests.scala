package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

import scala.collection.mutable.ListBuffer

class TraversableTests extends AnyFunSpec {
  describe("Traversables") {
    it("are a way to view sets") {
      val foo: Iterable[String] = Set("a" ,"b", "c")

      val collect = scala.collection.mutable.Set[String]()
      foo.foreach( collect += _ )
      collect shouldBe foo
    }

    it("- it's easy to roll your own") {
      val foo = new Iterable[String]() {
        override def iterator: Iterator[String] =
          Iterator("Hello", "Goodbye")
      }
      foo.toSet shouldBe Set("Hello", "Goodbye")
    }

    it("come with lots of added value") {
      val foo = new Iterable[Int]() {
        override def iterator: Iterator[Int] =
          Iterator(7, 9)
      }

      foo.toList shouldBe List(7, 9)
      foo ++ foo shouldBe List(7, 9, 7, 9)
      foo.foldLeft(3)(_ * _) shouldBe 189
      foo.filter(_ > 8) shouldBe List(9)
    }

    it("don't iterate any more than they have to") {
      val foo = new Iterable[Int]() {
        override def iterator: Iterator[Int] =
          Iterator(1,4,9)
      }
      val received = ListBuffer[Int]()
      foo.exists { p =>
        received += p
        p > 3
      } shouldBe true
      received shouldBe Seq(1, 4)
    }
  }
}
