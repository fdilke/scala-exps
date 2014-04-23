package com.fdilke.scala

import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory

/**
 * Author: fdilke
 */

//Considering quadratics of the form:
//
//n² + an + b, where |a| < 1000 and |b| < 1000
//
//where |n| is the modulus/absolute value of n
//e.g. |11| = 11 and |−4| = 4
//
//Find the product of the coefficients, a and b, for the quadratic expression that produces the
//maximum number of primes for consecutive values of n, starting with n = 0.

object QuadraticExpression {
  def apply(a : Int, b : Int) = new QuadraticExpression(a,b)
}

class QuadraticExpression(a : Int, b : Int) {
  def apply(n : Int) : Int = n*n + a*n + b
  def maxConsecutive : Int = (0 to b takeWhile {
    d : Int => Euler27.isPrime(this(d))
  }).size
}

object Euler27 extends App {
  def isPrime(n : Int) : Boolean = (n > 1) && (
    (2 until n) forall { n % _ > 0 }
  )

  var highScore = -1
  var best_a_b = (0, 0)
  for {a <- -999 to 999
       b <- -999 to 999} {
    val numPrimes = QuadraticExpression(a, b).maxConsecutive
    if (numPrimes > highScore) {
      println("new high score: (" + a + "," + b + ") => " + numPrimes)
      highScore = numPrimes
      best_a_b = (a, b)
    }
  }
  val (a, b) = best_a_b
  println("Best: " + best_a_b + "with " + highScore + " consecutive primes, product = " + (a*b))
}

class Euler27Test extends FunSuite with MockFactory {
  test("primality") {
    import Euler27._

    assert(((-3 to 12) filter isPrime) ===
      List(2,3,5,7,11)
    )
  }

  test("quadratic expressions: evaluation") {
    val q : QuadraticExpression = QuadraticExpression(2 ,-3) // n² + 2n - 3
    assert(q(5) === 32)
  }

  test("quadratic expressions: max consecutive primes") {
    assert(QuadraticExpression(2 ,-3).maxConsecutive === 0)
    assert(QuadraticExpression(2 ,3).maxConsecutive === 1)
    assert(QuadraticExpression(1 ,3).maxConsecutive === 2)
    assert(QuadraticExpression(1 ,5).maxConsecutive === 4)
    assert(QuadraticExpression(1 ,41).maxConsecutive === 40)
    assert(QuadraticExpression(-79, 1601).maxConsecutive === 80)
  }
}
