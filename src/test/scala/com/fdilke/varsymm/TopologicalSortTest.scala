package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._
//import scala.math.ordering._

object PartialOrderingSet {

 def apply[T]: PartialOrdering[Set[T]] =
   new PartialOrdering[Set[T]] {
    override def tryCompare(x: Set[T], y: Set[T]): Option[Int] =
      if (x == y)
        Some(0)
      else if (x.subsetOf(y))
        Some(-1)
      else if (y.subsetOf(x))
        Some(1)
      else
        None

     override def lteq(x: Set[T], y: Set[T]): Boolean =
       x.subsetOf(y)
   }
}

class TopologicalSortTest extends FunSpec {
  implicit val orderingInt: PartialOrdering[Int] =
    implicitly[Ordering[Int]]

  implicit val orderingSetInt: PartialOrdering[Set[Int]] =
    PartialOrderingSet[Int]

  describe("Topological sort") {
    it("works for an empty list") {
      TopologicalSort[Int](
        Seq.empty[Int]
      ) shouldBe
        Seq.empty
    }

    it("works for a list of integers") {
      TopologicalSort(
        Seq(6, 1, 3, 2)
      ) shouldBe
        Seq(1, 2, 3, 6)
    }

    it("works for a list of sets that can be nested") {
      TopologicalSort(Seq(
        Set(1, 6),
        Set(1, 2, 6),
        Set(1),
        Set(1, 2, 4, 6),
      )) shouldBe Seq(
        Set(1),
        Set(1, 6),
        Set(1, 2, 6),
        Set(1, 2, 4, 6),
      )
    }

    it("works for a list of incomparable sets") {
      val sets = Seq(
        Set(1, 6),
        Set(1, 2),
        Set(2, 6),
        Set(7),
      )
      TopologicalSort(sets) shouldBe sets
    }

    it("works for a list of partly-comparable sets that can't be nested") {
      TopologicalSort(Seq(
        Set(1, 6),
        Set(1, 2, 6),
        Set(1),
        Set(1, 2, 4, 6),
      )) shouldBe Seq(
        Set(1),
        Set(1, 6),
        Set(1, 2, 6),
        Set(1, 2, 4, 6),
      )
    }

    it("works for the complete list of subsets of a 3-set") {
      val sets = Seq[Set[Int]](
        Set(1, 2, 3),
        Set(1, 2),
        Set(1, 3),
        Set(2, 3),
        Set(1),
        Set(2),
        Set(3),
        Set()
      )
      val sorted = TopologicalSort(sets)
      sorted.toSet shouldBe sets.toSet
      for {
        i <- sorted.indices
        j <- sorted.indices if i < j
      }
        if (sorted(j).subsetOf(sorted(i)))
          fail(s"not sorted: ${sorted(j)} <= ${sorted(i)}")
    }
  }
}
