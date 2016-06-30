package com.fdilke.scala

import java.util.concurrent.atomic.AtomicInteger

import org.scalatest.{FreeSpec, Matchers}
import Matchers._

import scala.collection.SeqView

class ViewTest extends FreeSpec {
  "A view" - {
    "xx" in {
      val originalList = List(1, 2, 3)
      val view: SeqView[Int, List[Int]] =
        originalList.view

      val forced: List[Int] =
        view.force
      forced shouldBe originalList

      val count = new AtomicInteger(0)
      val lazilyMapped = view.map { i =>
        count.incrementAndGet()
      }
      count.get() shouldBe 0
      lazilyMapped.force shouldBe List(1,2,3)
    }
  }
}
