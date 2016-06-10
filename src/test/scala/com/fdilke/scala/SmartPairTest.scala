package com.fdilke.scala

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class SmartPairTest extends FreeSpec {

  case class x[A, B](_1: A, _2: B)

  implicit class RichObject[A](a: A) {
    def x[B](b: B) = new x(a, b)
  }

  "Smart pairs"- {
    "allow their components to be accessed" in {
      val x23: Int x String = 2 x "a"
      x23._1 shouldBe 2
      x23._2 shouldBe "a"
    }

    "have sane equality semantics" in {
      (2 x "a") shouldBe (2 x "a")
      (2 x "a") should not be (1 x "a")
      (2 x "a") should not be (2 x "b")
    }

    "can have their components extracted" in {
      val a x b = 2 x "t"
      a shouldBe 2
      b shouldBe "t"
    }

    "can be matched" in {
      2 x "t" match {
        case a x b =>
          a shouldBe 2
          b shouldBe "t"
      }
    }
  }
}
