package com.fdilke.scala

import org.scalatest.FlatSpec
import org.scalatest.matchers.{ShouldMatchers, ClassicMatchers}

// User: Felix Date: 10/03/2014 Time: 22:14

class FlatSpecTest extends FlatSpec with ShouldMatchers {
  it should "xxx" in {
    0 should not be 1
  }
}
