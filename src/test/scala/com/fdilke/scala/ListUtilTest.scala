package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._
import org.scalatest.funspec.AnyFunSpec

class ListUtilTest extends AnyFunSpec {

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
