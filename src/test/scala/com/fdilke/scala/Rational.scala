package com.fdilke.scala

/**
 * Author: fdilke
 */
class Rational(n : Int, d : Int,
               private[this] var g : Int) {
    g = gcd(n.abs, d.abs)
    val num = n/g
    val denom = d/g

  def gcd(a : Int, b : Int) : Int =
    if (b == 0) a else gcd(b, a % b)
}
