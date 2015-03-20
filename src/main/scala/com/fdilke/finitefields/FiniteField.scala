package com.fdilke.finitefields

import scala.tools.nsc.interpreter.InputStream

// extractor to tell if a number is a prime power
object NontrivialPrimePower {
  def unapply(n: Int): Option[(Int, Int)] =
    if (n < 1)
      None
    else (2 to n).find { d => 0 == n % d }.flatMap { p => {
      def logP: Int => Option[Int] = {
        case 1 => Some(0)
        case pr if pr % p == 0 => logP(pr/p).map { _ + 1 }
        case _ => None
      }
      logP(n) map { r => p -> r }
    }}
}

object FiniteField {

  println("Hello from FiniteFields")
  val stream : InputStream = getClass.getResourceAsStream("CPimport.txt")
  val lines = scala.io.Source.fromInputStream( stream ).getLines
  println("lines.size = " + lines.size)

  def GF: Int => FiniteField = {
    case NontrivialPrimePower(p, n) => new FiniteField
    case _ => throw new IllegalArgumentException
  }
}

class FiniteField {

}