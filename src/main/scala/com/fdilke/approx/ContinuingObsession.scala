package com.fdilke.approx

object ContinuingObsession extends App {
  println(ContinuedFraction(Math.PI, 6))

  val mu = Math.log(2) / Math.log(1.5)
  println(ContinuedFraction(mu, 8))
}
