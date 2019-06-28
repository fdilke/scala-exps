package com.fdilke.varsymm

import scala.annotation.tailrec

trait Group[T] { group =>
  val unit: T
  val elements: Set[T]
  def multiply(element1: T, element2 : T): T
  def invert(element: T): T

  lazy val order: Int =
    elements.size

  case class Subgroup(
     override val elements: Set[T]
  ) extends Group[T] {
    override final val unit: T = group.unit

    override final def multiply(element1: T, element2: T): T =
      group.multiply(element1, element2)

    override final def invert(element: T): T =
      group.invert(element)
  }

  lazy val trivialSubgroup: Subgroup =
    Subgroup(Set(unit))

  lazy val wholeGroup: Subgroup =
    Subgroup(elements)

  def generateSubgroup(generators: T*): Subgroup =
    generateSubgroup(generators.toSet)

  def generateSubgroup(generators: Set[T]): Subgroup = {
    def multiplySets(
      set1: Set[T],
      set2: Set[T]
    ): Set[T] = {
      for (x <- set1; y <- set2)
        yield group.multiply(x, y)
    }

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
}

object GroupSugar {
  implicit class MultiplicationWrapper[T : Group](element: T) {
    def *(element2: T): T =
      implicitly[Group[T]].multiply(element, element2)

    def unary_~ : T =
      implicitly[Group[T]].invert(element)
  }
}