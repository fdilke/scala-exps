package com.fdilke.finitefields

import org.scalatest.matchers.should.Matchers._
import org.scalatest.freespec.AnyFreeSpec

class TangentGroupExperimentTests extends AnyFreeSpec {

  "The arctan2 formula inspires a spiffy monoid" in {

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

  "we can construct a projective plane using projective triples" in {
      val q = 7
      val field = FiniteField.GF(q)
      import field.{I, O, RichElement}

      val II = I + I
      val III = I + I + I
      intercept[IllegalArgumentException] {
        field.projectiveTriple(O, O, O)
      }
      field.projectiveTriple(O, I, I) shouldBe
        field.projectiveTriple(O, II, II)

      field.projectiveTriple(I, O, I) shouldBe
        field.projectiveTriple(III, O, III)

      val triples = field.allProjectiveTriples

      triples.size shouldBe (q*q + q + 1)

      {
        for {
          a <- triples
          b <- triples
        } yield a == b
      }.count {
        identity
      } shouldBe triples.size

      for {
        a <- triples
      }
        triples.count{
          _ ⊥ a
        } shouldBe (q + 1)

      for {
        a <- triples
        b <- triples
      }
        (a ⊥ b) == (b ⊥ a)

      for {
        a <- triples
        b <- triples if a != b
      }
        triples.count { c =>
          (a ⊥ c) && (b ⊥ c)
        } shouldBe 1
  }

  "the plane construction does not contradict the Friendship Theorem" in {
    val q = 2
    val field = FiniteField.GF(q)

    val triples = field.allProjectiveTriples
    val tripleList = triples.toList

    for {
      a <- tripleList.indices
      b <- tripleList.indices
    }
      if (tripleList(a) ⊥ tripleList(b))
        println(s"$a ⊥ $b")
  }

  "a self-perpendicular triple exists in all cases" in {
    Seq(
      2, 3, 5, 7, 11, 25, 27
    ) foreach { q =>
      println("testing " + q)
      val field = FiniteField.GF(q)
      val triples = field.allProjectiveTriples

      assert {
        triples exists { a =>
          a ⊥ a
        }
      }
    }
  }
}
