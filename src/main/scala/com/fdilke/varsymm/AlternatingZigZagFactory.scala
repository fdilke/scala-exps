package com.fdilke.varsymm

import scala.language.postfixOps

trait ZigZag[
  L <: LatticeElement[L, I],
  I <: AnnotatedInclusion[L, I]
] {
  val isZig: Boolean
  val inclusion: I
  final lazy val next: L =
    if (isZig)
      inclusion.lower
    else
      inclusion.upper

  final lazy val isZag = !isZig
}

case class Zig[
  L <: LatticeElement[L, I],
  I <: AnnotatedInclusion[L, I]
](
  override val inclusion: I
) extends ZigZag[L, I] {
  override val isZig = true
}

case class Zag[
  L <: LatticeElement[L, I],
  I <: AnnotatedInclusion[L, I]
](
  override val inclusion: I
) extends ZigZag[L, I] {
  override val isZig = false
}

class AlternatingZigZagFactory[
  L <: LatticeElement[L, I],
  I <: AnnotatedInclusion[L, I]
](
  lattice: AnnotatedLattice[L, I],
  generator: () => Int
) {
  lazy val initialZig: ZigZag[L, I] =
    zigFrom(lattice.bottom)

  lazy val initialZag: ZigZag[L, I] =
    zagFrom(lattice.top)

  private def zigFrom(element: L): ZigZag[L, I] = {
    val inclusion = selectOne(
      element.strictlyAbove
    )
    Zig(
      inclusion
    )
  }

  private def zagFrom(element: L): ZigZag[L, I] = {
    val inclusion = selectOne(
      element.strictlyBelow
    )
    Zag(
      inclusion
    )
  }

  def apply(zigzag: ZigZag[L, I]): ZigZag[L, I] =
    if (zigzag.isZig)
      zagFrom(zigzag.inclusion.upper)
    else
      zigFrom(zigzag.inclusion.lower)

  private def selectOne[X](set: Set[X]): X =
    if (set.isEmpty)
      throw new IllegalArgumentException("Cannot select from empty set")
    else
      set.toSeq(generator() % set.size)
}
