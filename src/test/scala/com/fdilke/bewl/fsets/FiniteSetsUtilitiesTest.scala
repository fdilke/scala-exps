package com.fdilke.bewl.fsets
import FiniteSetsUtilities._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import ShouldMatchers._

// User: Felix Date: 28/05/2014 Time: 18:48

class FiniteSetsUtilitiesTest extends FunSpec {
  describe("cartesian products") {
    it("enumerate all sequences") {
      cartesian[Any](Seq(1, 2), Seq("a", "b"), Seq(true, false)).toList shouldBe List(
        Seq(1,"a",true), Seq(1,"a",false),
        Seq(1,"b",true), Seq(1,"b",false),
        Seq(2,"a",true), Seq(2,"a",false),
        Seq(2,"b",true), Seq(2,"b",false)
      )
    }
  }

  describe("allMaps()") {
    it("enumerates all maps between two sets") {
      allMaps(Seq(1, 2), Set("a", "b", "c")).toList shouldBe List(
        Map(1->"a", 2->"a"), Map(1->"b", 2->"a"), Map(1->"c", 2->"a"),
        Map(1->"a", 2->"b"), Map(1->"b", 2->"b"), Map(1->"c", 2->"b"),
        Map(1->"a", 2->"c"), Map(1->"b", 2->"c"), Map(1->"c", 2->"c")
      )
    }
  }
}
