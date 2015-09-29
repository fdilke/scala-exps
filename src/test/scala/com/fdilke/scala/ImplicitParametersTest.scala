package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import Matchers._

class ImplicitParametersTest extends FunSpec {
  describe("Implicit parameters") {
    it("can be passed to functions, provided you specify them in advance") {
      def foo(n: Int)(implicit prefix: String) = prefix + n

      implicit val useThisString: String = "UseThis"
      foo(2) shouldBe "UseThis2"
      // note, Ctrl-Shift-P inside the () will show the implicit parameter
    }
  }
}
