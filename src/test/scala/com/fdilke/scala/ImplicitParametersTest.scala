package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import Matchers._

class ImplicitParametersTest extends FunSpec {
  describe("Implicit parameters") {
    it("can be passed to functions, provided you specify them in advance") {
      def foo(n: Int)(implicit prefix: String) = prefix + n

      implicit val useThisString: String = "DefaultPrefix"
      foo(2) shouldBe "DefaultPrefix"
      // note, Ctrl-Shift-P inside the () will show the implicit parameter
    }

    it("include the identity function on any type") {
      implicitly[Int => Int].apply(2) shouldBe 2

      class Widget
      val widget = new Widget
      implicitly[Widget => Widget].apply(widget) shouldBe widget
    }
  }
}
