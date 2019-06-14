package com.fdilke.varsymm

trait Group[T] {
  val unit: T
  val elements: Traversable[T]
  def multiply(element1: T, element2 : T): T
}

object GroupSugar {
  implicit class MultiplicationWrapper[T : Group](element: T) {
    def *(element2: T): T =
      implicitly[Group[T]].multiply(element, element2)
  }
}