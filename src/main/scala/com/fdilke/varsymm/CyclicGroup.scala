package com.fdilke.varsymm

import scala.language.postfixOps

case class CyclicGroup(n: Int) extends Group[Int] {

  if (n <= 0)
    throw new IllegalArgumentException()

  override val unit: Int =
    0

  val generator: Int =
    1

  override val elements: Set[Int] =
    0 until n toSet

  override def multiply(element1: Int, element2: Int): Int =
    (element1 + element2) % n

  override def invert(element: Int): Int =
    if (element > 0)
      n - element
    else
      0
}
