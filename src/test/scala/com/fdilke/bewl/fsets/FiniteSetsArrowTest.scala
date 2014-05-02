package com.fdilke.bewl.fsets

import org.scalatest.{FunSpec, ShouldMatchers}

class FiniteSetsArrowTest extends FunSpec with ShouldMatchers {
  val dot = FiniteSetsDot("a", "b")
  val dotBig = FiniteSetsDot("a", "b", "c")
  val dotSmall = FiniteSetsDot("a")
  val doodah = FiniteSetsDot(1,2,3)
  val dash = FiniteSetsDot("X", "Y", "Z")
  val dashBig = FiniteSetsDot("X", "Y", "Z", "W")
  val dot2dash = FiniteSetsArrow(dot, dash, "a"->"X", "b"->"Y")
  val dot2dashBadValues = FiniteSetsArrow(dot, dash, "a"->true, "b"->FiniteSets)
  val dot2dash_2 = FiniteSetsArrow(dot, dash, "a"->"X", "b"->"Y")
  val dotSmall2dash = FiniteSetsArrow(dotSmall, dash, "a"->"X", "b"->"Y")
  val dot2dashBig = FiniteSetsArrow(dot, dashBig, "a"->"X", "b"->"Y")
  val dotBig2dash = FiniteSetsArrow(dotBig, dash, "a"->"X", "b"->"Y")
  val doodah2dot = FiniteSetsArrow(doodah, dot, 1->"a", 2->"b", 3->"a")

  describe("An arrow representing a morphism of finite sets") {
    it("should make accessible its source and target") {
      dot2dash.source should be (dot)
      dot2dash.target should be (dash)
    }

    it("should have sensible equality semantics") {
      dot2dash should be(dot2dash)
      dot2dash should be(dot2dash_2)
      dot2dash should not be dotSmall2dash
      dot2dash should not be dot2dashBig
    }

    it("should support sanity tests") {
      dot2dash.sanityTest()
      dot2dashBig.sanityTest()
      doodah2dot.sanityTest()

      intercept[IllegalArgumentException] {
        dotSmall2dash.sanityTest()
      }.getMessage should be("Map keys != source")

      intercept[IllegalArgumentException] {
        dotBig2dash.sanityTest()
      }.getMessage should be("Map keys != source")

      intercept[IllegalArgumentException] {
        dot2dashBadValues.sanityTest()
      }.getMessage should be("Map values not in target")
    }

    it("should compose with other appropriately conditioned arrows") {
      intercept[IllegalArgumentException] {
        dot2dash(dot2dash)
      }.getMessage should be("Target does not match source")

      dot2dash(doodah2dot) should be (
        FiniteSetsArrow(doodah, dash, 1->"X", 2->"Y", 3->"X")
      )
    }
  }
}
