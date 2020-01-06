package com.fdilke.yasu

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class CountUpToListTest extends AnyFunSpec {
  it("handles a list of length 0") {
    CountUpToList(List()) shouldBe
    List(
        List()
      )
  }

  it("handles a list of length 1") {
    CountUpToList(List(2)).toSet shouldBe
    Set(
      List(0),
      List(1),
      List(2)
    )
  }

  it("handles a list of length 2") {
    CountUpToList(List(2, 1)).toSet shouldBe
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
    CountUpToList(List(2, 1, 1)).toSet shouldBe
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
