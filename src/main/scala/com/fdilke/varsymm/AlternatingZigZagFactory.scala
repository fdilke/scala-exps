package com.fdilke.varsymm

trait ZigZag[L] {
  val isZig: Boolean
  val lower: L
  val upper: L
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
  override val isZig = true
}

case class AlternatingZigZagFactory[L <: LatticeElement[L]](
  lattice: AnnotatedLattice[L],
  generator: () => Int
) {
  val initialZig: ZigZag[L] =
    Zig(
      lattice.bottom,
      selectOne(lattice.bottom.strictlyAbove)
    )

  private def selectOne[X](set: Set[X]): X =
    if (set.isEmpty)
      throw new IllegalArgumentException("Cannot select from empty set")
    else
      set.toSeq(generator() % set.size)
}
