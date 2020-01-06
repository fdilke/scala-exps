package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._
import org.scalatest.flatspec.AnyFlatSpec

class FlatSpecTest extends AnyFlatSpec {
  it should "xxx" in {
    0 should not be 1
  }
}
