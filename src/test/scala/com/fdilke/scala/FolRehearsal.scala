package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._
import org.scalatest.freespec.AnyFreeSpec

// ∀∃

class FolRehearsal extends AnyFreeSpec {
  "Experiments with a first order logic DSL" - {
    "xx" in {
      val set: Set[Int] = Set(1,2,3)
      trait Doodad[T] {
        def first: T
      }
      case class Widget[T](
        set: Set[T]
      ) {
        def withFilter(dd: Doodad[T]): Widget[T] =
          this
        def map(ff: Doodad[T] => Boolean): Int =
          6
      }
      def representation[T](set: Set[T]): Widget[T] =
        Widget(set)
      object ∀ {
        def unapply[T](doodad: Doodad[T]): Option[T] =
          Some(doodad.first)
      }
      representation(set) map { doodad =>
        val t = ∀.unapply(doodad).head
        t > 2
      }
      // Now make this work:
      for {
        h <- representation[Int](set)
      } yield {
        h match {
          case ∀(x) =>
            x > 2
        }
      }
    }
  }
}
