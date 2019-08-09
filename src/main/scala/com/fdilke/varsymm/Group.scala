package com.fdilke.varsymm

import scala.annotation.tailrec

trait Group[T] { group =>
  val unit: T
  val elements: Set[T]
  def multiply(element1: T, element2 : T): T
  def invert(element: T): T

  final lazy val order: Int =
    elements.size

  final def conjugate(x: T, y : T): T =
    multiply(
      invert(y),
      multiply(x, y)
    )

  final case class Subgroup(
     override val elements: Set[T]
  ) extends Group[T] { subgroup =>
    final def isNormal: Boolean =
      group.elements.forall { x =>
        subgroup.elements.subsetOf(
          subgroup.elements.map { y =>
            group.conjugate(y, x)
          }
        )
      }

    override final val unit: T = group.unit

    override final def multiply(element1: T, element2: T): T =
      group.multiply(element1, element2)

    override final def invert(element: T): T =
      group.invert(element)

    final def contains(other: group.Subgroup): Boolean =
      other.elements.subsetOf(elements)

    final def ^(x: T): group.Subgroup =
      new group.Subgroup(
        elements.map {
          conjugate(_, x)
        }
      )
  }

  final lazy val trivialSubgroup: Subgroup =
    Subgroup(Set(unit))

  final lazy val wholeGroup: Subgroup =
    Subgroup(elements)

  final lazy val centre: Subgroup =
    Subgroup(
      elements filter { x =>
        elements forall { y =>
          group.commutes(x, y)
        }
      }
    )

  final def generateSubgroup(generators: T*): Subgroup =
    generateSubgroup(generators.toSet)

  private def multiplySets(
    set1: Set[T],
    set2: Set[T]
  ): Set[T] =
    for (x <- set1; y <- set2)
      yield group.multiply(x, y)

  final def generateSubgroup(generators: Set[T]): Subgroup = {
    @tailrec def generate(
      t: Set[T],
      x: Set[T]
    ): Set[T] = {
      val xg = multiplySets(x, generators)
      val xg_t = xg -- t
      if (xg_t.isEmpty)
        t
      else
        generate(t ++ xg_t, xg_t)
    }

    Subgroup(
      generate(generators, generators) + group.unit
    )
  }

  def orderOf(x: T): Int =
    generateSubgroup(x).order

  final def isCyclic: Boolean =
    elements.exists { candidateGenerator =>
      generateSubgroup(candidateGenerator) == wholeGroup
    }

  final def isAbelian: Boolean =
    elements.forall { x =>
      elements.forall { y =>
        commutes(x, y)
      }
    }

  final def commutes(x: T, y: T): Boolean =
    multiply(x, y) == multiply(y, x)

  trait AnnotatedSubgroup extends LatticeElement[
    AnnotatedSubgroup,
    AnnotatedSubgroupInclusion
  ] {
    val toSubgroup: Subgroup
    override val strictlyAbove: Set[AnnotatedSubgroupInclusion]
    override val strictlyBelow: Set[AnnotatedSubgroupInclusion]
  }

  trait AnnotatedSubgroupInclusion extends AnnotatedInclusion[
    AnnotatedSubgroup,
    AnnotatedSubgroupInclusion
  ]

  lazy val subgroups: Set[Subgroup] =
    EnumerateSubgroups(group)

  lazy val subgroupLattice: AnnotatedLattice[
    AnnotatedSubgroup,
    AnnotatedSubgroupInclusion
  ] =
    AnnotatedSubgroupLattice(group)

  case class Block(subgroups: Set[Subgroup]) {
    final val order: Int =
      subgroups.head.order
  }

  trait MarkTable {
    val blocks: Seq[Block]
    val marks: Seq[Seq[Int]]
  }

  lazy val markTable: MarkTable =
    MarkTable(group)
}

object GroupSugar {
  implicit class MultiplicationWrapper[
    T : Group
  ](
    element: T
  ) {
    def *(element2: T): T =
      implicitly[Group[T]].multiply(element, element2)

    def ^(element2: T): T =
      implicitly[Group[T]].conjugate(element, element2)

    def unary_~ : T =
      implicitly[Group[T]].invert(element)
  }
}