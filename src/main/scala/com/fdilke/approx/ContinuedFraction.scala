package com.fdilke.approx
import Rational.ONE

object ContinuedFraction {
  def apply(x: Double, depth: Int): ContinuedFraction = depth match {
    case 0 => new ContinuedFraction(x, Seq.empty, Seq.empty)
    case _ =>
      val xInt: Int = x.toInt
      val continuation = ContinuedFraction(1/(x - xInt), depth - 1)
      new ContinuedFraction(
        x,
        xInt +: continuation.coefficients,
        Rational(xInt) +: continuation.approximants.map { a =>
          Rational(xInt) + ONE / a
        }
      )
  }
}

class ContinuedFraction(
  val value: Double,
  val coefficients: Seq[Int],
  val approximants: Seq[Rational]
) {
  override val toString =
    s"""
       | $value ~= [${coefficients.mkString(" ")}]
       | ${approximants map { a => a.toString + "\t" + a.degreeApprox(value) } mkString("\n")}
     """.stripMargin
}
