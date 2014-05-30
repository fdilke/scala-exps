package com.fdilke.bewl.fsets

import org.scalatest.{FunSpec, ShouldMatchers}
import com.fdilke.bewl.fsets.FiniteSets.{FiniteSetsArrow, FiniteSetsDot}

class FiniteSetsArrowTest extends FunSpec with ShouldMatchers {
  val dot = FiniteSetsDot[String]("a", "b")
  val dotBig = FiniteSetsDot[String]("a", "b", "c")
  val dotSmall = FiniteSetsDot[String]("a")
  val doodah = FiniteSetsDot[Int](1,2,3)
  val dash = FiniteSetsDot[String]("X", "Y", "Z")
  val dashBig = FiniteSetsDot[String]("X", "Y", "Z", "W")
  val dot2dash = FiniteSetsArrow[String, String](dot, dash, "a"->"X", "b"->"Y")
  val dot2dashBadValues = FiniteSetsArrow[String, String](dot, dash, "a"->"boojum", "b"->"heejum")
  val dot2dash_2 = FiniteSetsArrow[String, String](dot, dash, "a"->"X", "b"->"Y")
  val dotSmall2dash = FiniteSetsArrow[String, String](dotSmall, dash, "a"->"X", "b"->"Y")
  val dot2dashBig = FiniteSetsArrow[String, String](dot, dashBig, "a"->"X", "b"->"Y")
  val dotBig2dash = FiniteSetsArrow[String, String](dotBig, dash, "a"->"X", "b"->"Y")
  val doodah2dot = FiniteSetsArrow[Int, String](doodah, dot, 1->"a", 2->"b", 3->"a")

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
