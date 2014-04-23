package com.fdilke.scala

import org.junit.{Assert, Test}
import Assert._
import MixCount._

/**
 * Author: fdilke
 * Counts the number of ways of mixing n traits.
 */

object MixCount {
  def factorial(n : Int) : Int = (1 to n)./:(1) { _*_ }
  def mixcount(n : Int) : Int = (0 to n).map { r => factorial(n)/factorial(r) } .sum
}

class MixCountTest {
  @Test def factorials() {
    assertEquals(Vector(1,1,2,6,24),
      (0 to 4) map factorial)
  }

  @Test def mixcounts() {
    assertEquals(Vector(1,2,5,16),
      (0 to 3) map mixcount)
  }
}
