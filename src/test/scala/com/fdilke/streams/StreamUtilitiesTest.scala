package com.fdilke.streams

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class StreamUtilitiesTest extends FreeSpec {

  val N: Stream[Int] = 0 #:: N.map(_ + 1)

  "All Pairs" - {
    "enumerates all pairs from a given stream" in {
      StreamUtilities.pairs(N, N map { _.toString }).take(10).toSeq shouldBe Seq(
        0 -> "0",
        0 -> "1",
        1 -> "0",
        0 -> "2",
        1 -> "1",
        2 -> "0",
        0 -> "3",
        1 -> "2",
        2 -> "1",
        3 -> "0"
      )
    }

    "has expected indices" in {
      StreamUtilities.indexPairs.take(6).toSeq shouldBe Seq(
        (0, 0),
        (0, 1), (1, 0),
        (0, 2), (1, 1), (2, 0)
      )
    }
   }
}