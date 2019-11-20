package com.fdilke.approx

object ContinuingObsession extends App {
  println(ContinuedFraction(Math.PI, 6))

  val mu = Math.log(2) / Math.log(1.5)
  println(ContinuedFraction(mu, 8))

  val gamma = Math.pow(2, 1/12.0)
  for { n <- 1 until 12 } {
    val gn = Math.pow(gamma, n)
    val frac = ContinuedFraction(gn, 6)
    val simplest: Rational = frac.approximants.find { _.denominator > 3 }.get
    println(s"g^$n\t$simplest")
  }
}
