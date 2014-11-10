package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import scala.reflect._
import scala.reflect.runtime.universe._
import Matchers._

class TypeAndClassTagTests extends FunSpec {
  describe("TypeTags") {
    it("can be obtained directly") {
      val intTypeTag = typeTag[Int]
      println("tag = " + intTypeTag.toString)
    }

    it("can be extracted implicitly from arguments") {
      def inspect[T: TypeTag](x: T) =
        typeOf[T] match {
          case TypeRef(id, symbol, args) =>
            s"type of $x has id $id, symbol $symbol, type arguments $args"
        }

      inspect(3)
      inspect(Array(5))
      inspect(typeTag[Int])
      inspect(classTag[Int])
      inspect((7, "seven"))

      //      class Foo[X]
      //      inspect(new Foo[(Unit, String, Int)])
    }

    it("can be used to tell what something is a list of") {
      def inspectList[A : TypeTag](xs: List[A]) = typeOf[A] match {
        case t if t =:= typeOf[String] => "list of strings"
        case t if t =:= typeOf[Int] => "list of ints"
        case t if t <:< typeOf[Traversable[_]] => "list of traversables"
        case _ => "Don't know!"
      }

      inspectList(List(1,2,3)) shouldBe "list of ints"
      inspectList(List("x", "y")) shouldBe "list of strings"
      inspectList(List(Seq())) shouldBe "list of traversables"
    }
  }

  describe("WeakTypeTags") {
    it("can be extracted implicitly from arguments") {
      def inspect[T: WeakTypeTag](x: T) =
        weakTypeOf[T] match {
          case TypeRef(id, symbol, args) =>
            s"weak type of $x has id $id, symbol $symbol, type arguments $args"
        }

      inspect(3)
      inspect(Array(5))
      inspect(typeTag[Int])
      inspect(classTag[Int])
      inspect((7, "seven"))

      //      class Foo[X]
      //      inspect(new Foo[(Unit, String, Int)])
    }
  }

  describe("ClassTags") {
    it("can be obtained directly") {
      val intClassTag = classTag[Int]
      println("tag = " + intClassTag.toString)

      intClassTag.canEqual(3) shouldBe false // ?
      intClassTag.canEqual("xxx") shouldBe false

      intClassTag.newArray(2) shouldBe Array[Int](0, 0)
      intClassTag.runtimeClass shouldBe 2.getClass
    }

    it("can be obtained implicitly from function arguments") {
        def inspect[T](x: T)(implicit tag: ClassTag[T]): Unit =
          println(s"$x has class tag $tag")

        inspect(3)
        inspect(Array(5))
        inspect(typeTag[Int])
        inspect(classTag[Int])
        inspect((7, "seven"))

        case class Foo[X](placeHolder: Any)
        inspect(new Foo[(Unit, String, Int)](0))
    }
  }
}
