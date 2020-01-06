package com.fdilke.streams

import org.scalatest.matchers.should.Matchers._
import org.scalatest.freespec.AnyFreeSpec

class StreamUtilitiesTest extends AnyFreeSpec {

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

  "Sequences" - {
    "enumerates all sequences of a given length" - {
      "for 0" in {
        StreamUtilities.sequencesOfLength(0)() shouldBe Seq(
          Seq()
        )
        StreamUtilities.sequencesOfLength(0)('a) shouldBe Seq(
          Seq()
        )
      }
      "for 1" in {
        StreamUtilities.sequencesOfLength(1)() shouldBe Seq(
        )
        StreamUtilities.sequencesOfLength(1)('a) shouldBe Seq(
          Seq('a)
        )
        StreamUtilities.sequencesOfLength(1)('a, 'b) shouldBe Seq(
          Seq('a),
          Seq('b)
        )
      }
      "for 2" in {
        StreamUtilities.sequencesOfLength(2)() shouldBe Seq(
        )
        StreamUtilities.sequencesOfLength(2)('a) shouldBe Seq(
          Seq('a, 'a)
        )
        StreamUtilities.sequencesOfLength(2)('a, 'b) shouldBe Seq(
          Seq('a, 'a),
          Seq('b, 'a),
          Seq('a, 'b),
          Seq('b, 'b)
        )
      }
      "for 3" in {
        StreamUtilities.sequencesOfLength(3)() shouldBe Seq(
        )
        StreamUtilities.sequencesOfLength(3)('a) shouldBe Seq(
          Seq('a, 'a, 'a)
        )
        StreamUtilities.sequencesOfLength(3)('a, 'b) shouldBe Seq(
          Seq('a, 'a, 'a),
          Seq('b, 'a, 'a),
          Seq('a, 'b, 'a),
          Seq('b, 'b, 'a),
          Seq('a, 'a, 'b),
          Seq('b, 'a, 'b),
          Seq('a, 'b, 'b),
          Seq('b, 'b, 'b)
        )
      }
    }
    "enumerates all sequences from a given base list" - {
      "for empty" in {
        StreamUtilities.sequences() shouldBe Seq(
          Seq()
        )
      }
      "for a singleton" in {
        StreamUtilities.sequences(0).take(4) shouldBe Seq(
          Seq(), Seq(0), Seq(0, 0), Seq(0, 0, 0)
        )
      }
      "for a doubleton" in {
        StreamUtilities.sequences(0, 1).take(7) shouldBe Seq(
          Seq(),
          Seq(0), Seq(1),
          Seq(0, 0), Seq(1, 0), Seq(0, 1), Seq(1, 1)
        )
      }
    }
  }
}