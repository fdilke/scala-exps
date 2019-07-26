package com.fdilke.varsymm

case class Matrix22(
  a11: Double,
  a12: Double,
  a21: Double,
  a22: Double
) {
  def *(that: Matrix22): Matrix22 =
    Matrix22(
      a11*that.a11 + a12*that.a21,
      a11*that.a12 + a12*that.a22,
      a21*that.a11 + a22*that.a21,
      a21*that.a12 + a22*that.a22
    )
}

object Matrix22 {
  val identity: Matrix22 =
    Matrix22(1, 0, 0, 1)

  def withinTolerance(
     matrix: Matrix22,
     matrix2: Matrix22,
     tolerance: Double
  ) : Boolean =
    withinTolerance(matrix.a11, matrix2.a11, tolerance) &&
    withinTolerance(matrix.a12, matrix2.a12, tolerance) &&
    withinTolerance(matrix.a21, matrix2.a21, tolerance) &&
    withinTolerance(matrix.a22, matrix2.a22, tolerance)

  def withinTolerance(
    value: Double,
    value2: Double,
    tolerance: Double
 ) : Boolean =
    Math.abs(value - value2) < tolerance

  def rotation(theta: Double): Matrix22 = {
    val c = Math.cos(theta)
    val s = Math.sin(theta)
    Matrix22(c, -s, s, c)
  }
}