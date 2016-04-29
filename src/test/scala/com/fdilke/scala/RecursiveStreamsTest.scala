package com.fdilke.scala

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

import scala.Function.tupled

class RecursiveStreamsTest extends FreeSpec {
  "Recursive streams" - {
    "can be used to model infinite lists" in {
      lazy val N: Stream[Int] = 0 #:: N.map(_ + 1)
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
  }
}
