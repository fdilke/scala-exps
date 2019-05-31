package com.fdilke.wordgraph

import org.scalatest.{FunSpec, Matchers}
import Matchers._

class PositionTest extends FunSpec {
  describe("Verifying WordGraph positions for P") {
    it("has the right basic properties") {
      Position(Set.empty, None) shouldBe 'P
      Position(Set("foo"), None) should not be 'P
    }
  }

}
