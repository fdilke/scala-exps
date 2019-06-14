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

  def inverse: Permutation = {
    val array: Array[Int] = new Array[Int](degree)
    0 until degree foreach { index =>
      array(this(index)) = index
    }
    Permutation(array :_*)
  }
}

object Permutation {
  def identity(degree: Int): Permutation =
    Permutation(
      0 until degree :_*
    )

  def enumerate(degree: Int): Seq[Permutation] =
    (0 until degree permutations) map {
      Permutation(_ :_*)
    } toSeq

  def group(degree: Int): Group[Permutation] =
    new Group[Permutation] {
      override lazy val unit: Permutation =
        Permutation.identity(degree)

      override lazy val elements: Seq[Permutation] =
        enumerate(degree)

      override def multiply(
        p1: Permutation,
        p2: Permutation
      ): Permutation =
        p1(p2)

      override def invert(element: Permutation): Permutation =
        element.inverse
    }
}
