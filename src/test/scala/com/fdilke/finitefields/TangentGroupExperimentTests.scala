package com.fdilke.finitefields

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class TangentGroupExperimentTests extends FreeSpec {

  "The arctan2 formula inspires a spiffy monoid" - {

    case class Atan2(
      p: Int,
      P: Int
    ) {
      def *(other: Atan2) =
        other match {
          case Atan2(q, qq) =>
            val Q = qq
            Atan2(
              p * Q + P * q,
              P * Q - p * q
            )
        }
    }

    val o = Atan2(0, 0)
    val i = Atan2(0, 1)
    val a = Atan2(2, 3)
    val b = Atan2(4, 5)
    val c = Atan2(6, 7)

    o * a shouldBe o
    i * a shouldBe a
    a * b shouldBe (b * a)
    (a * b) * c shouldBe {
      a * (b * c)
    }
  }
}
