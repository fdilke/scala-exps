package com.fdilke.scala

import org.scalatest.{FreeSpec, Matchers}
import Matchers._

class TraitCompositionTest extends FreeSpec {
  "Traits" - {
    "compose as expected" in {
      class IntHandler {
        def view(n: Int) =
          n

        def toString(n: Int) =
          view(n).toString
      }

      trait Incrementing extends IntHandler {
        override def view(n: Int) =
          super.view(n) + 1
      }

      trait Doubling extends IntHandler {
        override def view(n: Int) =
          super.view(n) * 2
      }

      val handler = new IntHandler with
        Incrementing with Doubling

      handler.toString(7) shouldBe "16"
    }
  }
}
