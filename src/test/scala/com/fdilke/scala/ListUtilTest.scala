package com.fdilke.scala

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class ListUtilTest extends FunSpec {

  private val fruits : List[String] = List(
    "apple", "orange", "banana", "pear"
  )

  describe("The utility") {
    it("should enable counting of elements satisfying a predicate") {
      ListUtil.count(fruits, hasMoreLettersThan(5)) should be (2)
    }
  }

  private def hasMoreLettersThan(numLetters : Int) : (String => Boolean) = {
//    def ourFunction(string : String) : Boolean =
//      string.length > numLetters
//
//    ourFunction
    _.length > numLetters
  }
}
