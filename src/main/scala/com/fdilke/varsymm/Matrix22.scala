package com.fdilke.varsymm

case class Matrix22(
  a11: Double,
  a12: Double,
  a21: Double,
  a22: Double
) {

}

object Matrix22 {
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
}