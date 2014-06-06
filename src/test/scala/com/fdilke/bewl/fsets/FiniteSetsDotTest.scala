package com.fdilke.bewl.fsets

import org.scalatest.{ShouldMatchers, FunSpec}
import com.fdilke.bewl.fsets.FiniteSets.{FiniteSetsArrow, FiniteSetsDot}

class FiniteSetsDotTest extends FunSpec with ShouldMatchers {
  describe("A dot representing a finite set") {
    it("should have sensible equality semantics") {
      val dot = FiniteSetsDot[String]("a", "b")
      val dash = FiniteSetsDot[String]("a", "b")
      val doodah = FiniteSetsDot[String]("a", "b", "c")

      dot should be(dot)
      dot should be(dash)
      dot should not be doodah
    }

    it("should have an identity arrow") {
      val dot = FiniteSetsDot[String]("a", "b")
      dot.identity should be(
        FiniteSetsArrow[String, String](dot, dot, Map("a" -> "a", "b" -> "b"))
      )
    }
  }
}
