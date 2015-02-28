package com.fdilke.approx

object Rational {
  def apply(n: Int) = new Rational(n, 1)
  def apply(n: Int, d: Int) = {
    val g = gcd(n, d)
    new Rational(n/g, d/g)
  }

  def gcd(a : Int, b : Int) : Int =
    if (b == 0) a else gcd(b, a % b)

  def unapply(r: Rational): Option[(Int, Int)] = Some((r.numerator, r.denominator))

  def ONE = Rational(1)
}

class Rational(val numerator: Int, val denominator: Int) {

  def +(that: Rational) = Rational(
    numerator * that.denominator + that.numerator * denominator,
    denominator * that.denominator
  )

  def *(that: Rational) = Rational(
    numerator * that.numerator,
    denominator * that.denominator
  )

  def /(that: Rational) = Rational(
    numerator * that.denominator,
    denominator * that.numerator
  )

  override def equals(that: Any): Boolean = that match {
    case Rational(n, d) => (n == numerator) && (d == denominator)
  }

  override val toString = s"$numerator/$denominator"

  val toDouble = numerator.toDouble / denominator

  // Calculate the degree k of approximation: | x - n/d | = 1/kd^2
  def degreeApprox(value: Double)  =
    1 / (Math.abs(value - toDouble) * denominator * denominator)
}
