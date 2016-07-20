package com.fdilke.yasu

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class ListMapTest extends FunSpec {
  it("handles a list of length 0") {
    TheListMap(List()) shouldBe
      List(
        List()
      )
  }

  it("handles a list of length 1") {
    TheListMap(List(2)).toSet shouldBe
    Set(
      List(0),
      List(1),
      List(2)
    )
  }

  it("handles a list of length 2") {
    TheListMap(List(2, 1)).toSet shouldBe
    Set(
      List(0, 0),
      List(0, 1),
      List(1, 0),
      List(1, 1),
      List(2, 0),
      List(2, 1)
    )
  }

  it("handles a list of length 3") {
    TheListMap(List(2, 1, 1)).toSet shouldBe
    Set(
      List(0, 0, 0),
      List(0, 0, 1),
      List(0, 1, 0),
      List(0, 1, 1),
      List(1, 0, 0),
      List(1, 0, 1),
      List(1, 1, 0),
      List(1, 1, 1),
      List(2, 0, 0),
      List(2, 0, 1),
      List(2, 1, 0),
      List(2, 1, 1)
    )
  }
//        List(0,0,0),
//        List(0,0,1),
//        List(0,1,0),
//        List(0,1,1),
//        List(0,1,1),
//        List(1,0,0),
//        List(0,1,1),

}
