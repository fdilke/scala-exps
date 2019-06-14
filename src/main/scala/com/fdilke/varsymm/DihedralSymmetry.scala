package com.fdilke.varsymm

import com.fdilke.varsymm.DihedralSymmetry.signOf

object DihedralSymmetry {
  val unit: DihedralSymmetry =
    DihedralSymmetry(reflect=false, 0)

  def signOf(reflect: Boolean): Int =
    if (reflect)
      -1
    else
      +1
}

case class DihedralSymmetry(
 reflect: Boolean,
 shift : Int
) {
  def invert(modulus: Int): DihedralSymmetry =
    if (reflect)
      this
    else
      DihedralSymmetry(reflect=false, modulus - shift)

  def compose(
     other: DihedralSymmetry,
     modulus: Int
  ): DihedralSymmetry =
    DihedralSymmetry(
      reflect= reflect ^ other.reflect,
      (
        (signOf(other.reflect) * shift) +
          other.shift + modulus
      ) % modulus
    )
}
