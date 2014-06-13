package com.fdilke.scala

import org.junit.Assert._
import org.scalatest.FunSpec
import org.scalatest.Matchers._

class PartialFunctionTests extends FunSpec {

  describe("A partial function") {
    it("can be used to collect values from a list") {
      val list = List(Some("Kind"), None, Some("Dog"))
      list.collect {
        case Some(string) => string.length()
      } should be(List(4,3))
    }

    it("can be used to collect values from a map") {
      val map = Map(2->"hello", 3->"goodbye", 4->"xxx")
      map.collect {
        case ((value : Int), message) if value % 2 == 0 => s"$message+$message"
      } should be (List("hello+hello", "xxx+xxx"))
    }

    it("knows when it is undefined") {
      val partialFunc : PartialFunction[Int, String] = {
        case value : Int if value % 2 == 0 => s"result$value"
      }
      assertFalse(partialFunc.isDefinedAt(1))
      assert(partialFunc.isDefinedAt(2))
    }

    it("can be hand rolled without a case statement") {
      val partialFunc = new PartialFunction[Int, String] {
        override def isDefinedAt(value: Int): Boolean = value % 2 == 0
        override def apply(value: Int): String = s"result$value"
      }
      partialFunc(1) should be("result1") // correct behaviour even though it's not defined :(
      partialFunc(2) should be("result2")
      List(1,2,3,4) collect partialFunc should be(
        List("result2", "result4")
      )
    }

    it("throws an exception when invoked incorrectly - as least, if using case")  {
      val partialFunc : PartialFunction[Int, String] = {
        case value : Int if value % 2 == 0 => s"result$value"
      }
      intercept[MatchError] {
        partialFunc(1)
      }
      partialFunc(2) should be("result2")
    }
  }
}