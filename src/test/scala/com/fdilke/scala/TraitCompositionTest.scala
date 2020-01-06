package com.fdilke.scala

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers._

class TraitCompositionTest extends AnyFreeSpec {
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

    "can be stacked easily on top of existing methods" in {
      trait IntHandler {
        def handle(n: Int): String
      }

      trait IntHandlerIncrementer extends IntHandler {
        abstract override def handle(n: Int): String =
          super.handle(n + 1)
      }

      class MyIntHandler extends IntHandler  {
        def handle(n: Int): String =
          n.toString
      }
      val handler = new MyIntHandler with IntHandlerIncrementer

      //      // What we'd like to do but can't:
      //
      //      val handler = new IntHandler with IntHandlerIncrementer {
      //        override def handle(n: Int): String =
      //          n.toString
      //      }

      handler.handle(3) shouldBe "4"
    }
  }
}
