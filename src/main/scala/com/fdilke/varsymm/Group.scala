package com.fdilke.varsymm

import scala.annotation.tailrec

trait Group[T] { group =>
  val unit: T
  val elements: Set[T]
  def multiply(element1: T, element2 : T): T
  def invert(element: T): T

  lazy val order: Int =
    elements.size

  final def conjugate(x: T, y : T): T =
    multiply(
      invert(y),
      multiply(x, y)
    )

  case class Subgroup(
     override val elements: Set[T]
  ) extends Group[T] { subgroup =>
    final def isNormal: Boolean = {
      group.elements.forall { x =>
        subgroup.elements.subsetOf(
          subgroup.elements.map { y =>
            group.conjugate(y, x)
          }
        )
      }
    }

    override final val unit: T = group.unit

    override final def multiply(element1: T, element2: T): T =
      group.multiply(element1, element2)

    override final def invert(element: T): T =
      group.invert(element)

    def contains(other: group.Subgroup): Boolean =
      other.elements.subsetOf(elements)
  }

  lazy val trivialSubgroup: Subgroup =
    Subgroup(Set(unit))

  lazy val wholeGroup: Subgroup =
    Subgroup(elements)

  lazy val centre: Subgroup =
    Subgroup(
      elements filter { x =>
        elements forall { y =>
          group.commutes(x, y)
        }
      }
    )

  def generateSubgroup(generators: T*): Subgroup =
    generateSubgroup(generators.toSet)

  private def multiplySets(
    set1: Set[T],
    set2: Set[T]
  ): Set[T] =
    for (x <- set1; y <- set2)
      yield group.multiply(x, y)

  def generateSubgroup(generators: Set[T]): Subgroup = {
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

  def isCyclic: Boolean =
    elements.exists { candidateGenerator =>
      generateSubgroup(candidateGenerator) == wholeGroup
    }

  def isAbelian: Boolean =
    elements.forall { x =>
      elements.forall { y =>
        commutes(x, y)
      }
    }

  def commutes(x: T, y: T): Boolean =
    multiply(x, y) == multiply(y, x)

  trait AnnotatedSubgroup {
    val toSubgroup: Subgroup
    val strictlyAbove: Set[AnnotatedSubgroup]
    val strictlyBelow: Set[AnnotatedSubgroup]
  }
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