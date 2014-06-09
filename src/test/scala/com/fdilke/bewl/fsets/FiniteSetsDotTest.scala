package com.fdilke.bewl.fsets

import org.scalatest.{ShouldMatchers, FunSpec}
import com.fdilke.bewl.fsets.FiniteSets.{FiniteSetsArrow, FiniteSetsDot}

class FiniteSetsDotTest extends FunSpec with ShouldMatchers {
  describe("A dot representing a finite set") {
    it("should have partly sensible equality semantics") {
      val dot = FiniteSetsDot[String]("a", "b")
      val dash = FiniteSetsDot[String]("a", "b")
      val doodah = FiniteSetsDot[String]("a", "b", "c")

      dot shouldBe dot
      dot should not be dash
      dot should not be doodah
    }

    it("should have an identity arrow") {
      val dot = FiniteSetsDot[String]("a", "b")
      dot.identity shouldBe
        FiniteSetsArrow[String, String](dot, dot, "a" -> "a", "b" -> "b")
    }
  }
}
