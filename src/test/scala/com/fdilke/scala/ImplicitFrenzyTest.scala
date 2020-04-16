package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class ImplicitFrenzyTest extends AnyFunSpec {
  describe("Implicits support") {
    it("conversions with implicit parameters") {
      case class Prefix(prefix: String)
      object Scope {

        implicit class Foo(text: String) {
          def extensionMethod()(implicit prefix: Prefix) =
            (prefix.prefix + text).length
        }

      }
      import Scope._
      implicit val prefix: Prefix = Prefix("//")
      "xyz".extensionMethod() shouldBe 5
    }
    it("automatic use of builder functions if available") {
      trait Wellness[T]
      object Wellness {
        implicit def standardWellness[T]: Wellness[T] =
          new Wellness[T] {}
      }
      val wellness: Wellness[Seq[Int]] =
        implicitly[Wellness[Seq[Int]]]
    }
  }
}
