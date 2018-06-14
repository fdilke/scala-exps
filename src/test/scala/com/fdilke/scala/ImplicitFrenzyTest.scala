package com.fdilke.scala

import org.scalatest.{FunSpec, Matchers}
import Matchers._

class ImplicitFrenzyTest extends FunSpec {
  describe("Implicit conversions with implicit parameters") {
    it("work just fine") {
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
  }
}
