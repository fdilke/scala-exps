package com.fdilke.scala

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class FlatSpecTest extends FlatSpec {
  it should "xxx" in {
    0 should not be 1
  }
}
