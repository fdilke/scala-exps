package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._
import org.scalatest.freespec.AnyFreeSpec

import scala.Function.tupled

class RecursiveStreamsTest extends AnyFreeSpec {
  "Recursive streams" - {
    "can be used to model infinite lists" in {
      lazy val N: Stream[Int] = 0 #:: N.map { _ + 1 }
      (N take 4).toSeq shouldBe (0 until 4)
    }

    "can be used to calculate recurrences" in {
      lazy val fibs: Stream[Int] =
        0 #:: 1 #:: (
          fibs zip fibs.tail map tupled {
            _ + _
          }
        )

      fibs.take(8).toList shouldBe Seq(
        0, 1, 1, 2, 3, 5, 8, 13
      )
    }

    "can be used to simulate Eratosthanes' sieve" in {
      lazy val N: Stream[Int] = 0 #:: N.map { _ + 1 }
      lazy val primes: Stream[Int] =
        2 #:: (
          N map {
            _ + 3
          } filterNot { n =>
            primes takeWhile { p =>
              p * p <= n
            } exists {
              n % _ == 0
            }
          }
        )

      primes.take(8).toList shouldBe Seq(
        2, 3, 5, 7, 11, 13, 17, 19
      )
    }
  }
}
