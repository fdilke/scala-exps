package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

import scala.annotation.tailrec

object Collatz {
  def iterate(n: Int) =
    if (n % 2 == 1)
      (3 * n) + 1
    else
      n/2

  @tailrec
  def iterations(n: Int, accumulate: Int = 0): Int =
    if (n == 1)
      accumulate
    else
      iterations(iterate(n), accumulate + 1)
}

class CollatzConjectureTest extends AnyFunSpec {
  it("should give the right number of iterations") {
    Collatz.iterations(1) shouldBe 0
    Collatz.iterations(2) shouldBe 1

    var prevMax = 0
    (1 to 100000) foreach { n =>
      val it = Collatz.iterations(n)
      if (it > prevMax) {
        println(s"$n: \t $it")
        prevMax = it
      }
    }
  }
}
