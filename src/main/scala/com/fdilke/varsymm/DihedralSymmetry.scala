package com.fdilke.varsymm

import com.fdilke.varsymm.DihedralSymmetry.signOf

object DihedralSymmetry {
  def unit(modulus: Int): DihedralSymmetry =
    DihedralSymmetry(modulus, reflect=false, 0)

  def signOf(reflect: Boolean): Int =
    if (reflect)
      -1
    else
      +1
}

case class DihedralSymmetry(
 modulus: Int,
 reflect: Boolean,
 shift : Int
) {
  def toMatrix : Matrix22 = {
    val theta = shift * (2 * Math.PI / modulus)
    val rotation = Matrix22.rotation(theta)
    if (reflect)
      Matrix22.reflection * rotation
    else
      rotation
  }

  def invert: DihedralSymmetry =
    if (reflect)
      this
    else if (shift == 0)
      DihedralSymmetry.unit(modulus)
    else
      DihedralSymmetry(modulus, reflect=false, modulus - shift)

  def *(
     other: DihedralSymmetry
  ): DihedralSymmetry =
    if (modulus == other.modulus)
      DihedralSymmetry(
        modulus,
        reflect = reflect ^ other.reflect,
        (
          (signOf(other.reflect) * shift) +
            other.shift + modulus
        ) % modulus
      )
    else
      throw new IllegalArgumentException("different moduli - can't compose")
}
