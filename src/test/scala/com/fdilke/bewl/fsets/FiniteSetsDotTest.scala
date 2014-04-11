package com.fdilke.bewl.fsets

import org.scalatest.{ShouldMatchers, FunSpec}

class FiniteSetsDotTest extends FunSpec with ShouldMatchers {
  describe("A dot representing a finite set") {
    it("should have sensible equality semantics") {
      val dot = FiniteSetsDot.from("a", "b")
      val dash = FiniteSetsDot.from("a", "b")
      val doodah = FiniteSetsDot.from("a", "b", "c")

      dot should be(dot)
      dot should be(dash)
      dot should not be doodah
    }

    it("should have an identity arrow") {
      val dot = FiniteSetsDot.from("a", "b")
      dot.identity should be(
        FiniteSetsArrow.from(dot, dot, "a" -> "a", "b" -> "b")
      )
    }
  }
}
