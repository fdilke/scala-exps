package com.fdilke.varsymm

case class Permutation(
  images: Int*
) {
  val degree = images.size

  def apply(index: Int): Int =
    images(index)

  def apply(other: Permutation): Permutation =
    if (degree == other.degree)
      Permutation(
        images map { other(_) } :_*
      )
    else throw new IllegalArgumentException("degrees differ, cannot compose")
}

object Permutation {
  def identity(degree: Int): Permutation =
    Permutation(
      0 until degree :_*
    )

  def enumerate(degree: Int): Traversable[Permutation] =
    (0 until degree permutations) map {
      Permutation(_ :_*)
    } toTraversable
}
