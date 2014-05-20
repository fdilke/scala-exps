package com.fdilke.util

object FancyMultiplicationImplicits {
  implicit def Int2SpecialMult(value : Int) =
    new FancyMultiplicationWrapper(value)
}

class FancyMultiplicationWrapper(left: Int) {

  private object Twice {
    def unapply(n: Int) = if (n % 2 == 0) Some(n / 2) else None
  }

  import FancyMultiplicationImplicits._

  def **(right: Int): Int = left match {
    case 0 => 0
    case Twice(n) => 2 * (n ** right)
    case _ => right ^ (2 * ((left / 2) ** right))
  }
}


