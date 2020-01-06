package com.fdilke.scala

import java.util.concurrent.atomic.AtomicInteger

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers._

import scala.collection.SeqView

class ViewTest extends AnyFreeSpec {
  "A view" - {
    "xx" in {
      val originalList = List(1, 2, 3)
      val view: SeqView[Int] =
        originalList.view

      val count = new AtomicInteger(0)
      val lazilyMapped: SeqView[Int] =
        view.map { i =>
          count.incrementAndGet()
        }
      count.get() shouldBe 0
      lazilyMapped.toSeq shouldBe Seq(1,2,3)
      count.get() shouldBe 3
    }
  }
}
