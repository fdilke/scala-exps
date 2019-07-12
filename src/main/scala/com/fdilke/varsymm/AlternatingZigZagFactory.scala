package com.fdilke.varsymm

trait ZigZag[L] {
  val isZig: Boolean
  val lower: L
  val upper: L

  final lazy val isZag = !isZig
}

case class Zig[L](
  override val lower: L,
  override val upper: L
) extends ZigZag[L] {
  override val isZig = true
}

case class Zag[L](
  override val upper: L,
  override val lower: L
) extends ZigZag[L] {
  override val isZig = false
}

class AlternatingZigZagFactory[L <: LatticeElement[L]](
  lattice: AnnotatedLattice[L],
  generator: () => Int
) {
  val initialZig: ZigZag[L] =
    zigFrom(lattice.bottom)

  val initialZag: ZigZag[L] =
    zagFrom(lattice.top)

  private def zigFrom(element: L): ZigZag[L] =
    Zig(
      element,
      selectOne(element.strictlyAbove)
    )

  private def zagFrom(element: L): ZigZag[L] =
    Zag(
      element,
      selectOne(element.strictlyBelow)
    )

  def apply(zigzag: ZigZag[L]): ZigZag[L] =
    if (zigzag.isZig)
      zagFrom(zigzag.upper)
    else
      zigFrom(zigzag.lower)

  private def selectOne[X](set: Set[X]): X =
    if (set.isEmpty)
      throw new IllegalArgumentException("Cannot select from empty set")
    else
      set.toSeq(generator() % set.size)
}
