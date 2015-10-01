package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import Matchers._

class ExtractorTest extends FunSpec {
  describe("extractors") {
    it("can be invoked from regexes") {
      val propertyRegex = "(.*?)=(.*)".r
      val property = "shoeSize=5"

      val propertyRegex(key, value) = property
      key shouldBe "shoeSize"
      value shouldBe "5"
    }

    it("can perform a boolean test") {
      object Even {
        def unapply(n: Int):Boolean =
          n % 2 == 0
      }
      intercept[MatchError] {
        3 match {
          case Even() =>
        }
      }
      2 match { case Even() => }
    }

    it("can extract a single value") {
      object Twice {
        def unapply(n: Int): Option[Int] = if (n % 2 == 0) Some(n / 2) else None
      }

      val Twice(m) = 6
      m shouldBe 3

      intercept[MatchError] {
        val Twice(q) = 5
      }
    }

    it("can extract several values") {
      object PrimePower {
        def unapply(n : Int) : Option[(Int, Int)] =
          if (n == 8)
            Some(2, 3)
        else None  // simplistic - only handles case 2^3
      }

      val PrimePower(p, n) = 8
      p shouldBe 2
      n shouldBe 3
    }

    it("can extract a variable number of values") {
      object PossibleTriplet {
        def unapplySeq(s: List[Int]) : Option[(Int, Seq[Int])] =
          if (s.size == 3)
            Some((s(0), s.slice(1, s.size)))
          else
            None
      }

      val PossibleTriplet(a,b,c) = List(4,5,6)
      (a, b, c) shouldBe (4,5,6)
    }

    it("can implicitly extract members from a case class with generics") {
      case class Pair[T <: Comparable[T]](left: T, right: T)
      Pair("a", "b") match {
        case Pair(x, y) =>
          x shouldBe "a"
          y shouldBe "b"
        case _ =>
          fail("can't apply extractor")
      }
    }
  }
}
