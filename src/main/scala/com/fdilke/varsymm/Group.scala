package com.fdilke.varsymm

trait Group[T] { group =>
  val unit: T
  val elements: Traversable[T]
  def multiply(element1: T, element2 : T): T
  def invert(element: T): T

  lazy val order: Int =
    elements.size

  case class Subgroup(
     override val elements: Traversable[T]
  ) extends Group[T] {
    override final val unit: T = group.unit

    override final def multiply(element1: T, element2: T): T =
      group.multiply(element1, element2)

    override final def invert(element: T): T =
      group.invert(element)
  }

  lazy val trivialSubgroup: Subgroup =
    Subgroup(Seq(unit))
}

object GroupSugar {
  implicit class MultiplicationWrapper[T : Group](element: T) {
    def *(element2: T): T =
      implicitly[Group[T]].multiply(element, element2)

    def unary_~ : T =
      implicitly[Group[T]].invert(element)
  }
}