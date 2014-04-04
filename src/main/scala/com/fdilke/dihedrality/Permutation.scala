package com.fdilke.dihedrality

import scala.math.max

object Permutation {
  def identity(numSymbols : Int) = new Permutation(Seq.tabulate(numSymbols) { x : Int => x })

  def apply(values:Int*) = new Permutation(canonical(values:_*))

  def canonical(values:Int*) = values slice(0, essentialDegree(values:_*))

  def essentialDegree(values:Int*) =
    (0 until values.length reverse) find {
      n => values(n) != n
    } match {
      case None => 0
      case Some(n) => n + 1
    }
}

class Permutation(val values: Seq[Int]) {
  def apply(that : Permutation) : Permutation = {
    val maxDegree = max(degree, that.degree)
    val composite = { n : Int => apply(that.apply(n))}
    new Permutation(0 until maxDegree map composite)
  }

  def apply(n : Int) : Int = if (n < degree) values(n) else n

  def parity = {
    val array = values.toArray

    var flag = 1
    for (index <- 0 to array.length-1) {
      if (array(index) != index) {
        var other = index
        while (index != array(other)) {
          other = array(other)
        }
        array(other) = array(index)
        array(index) = index
        flag = -flag
      }
    }
    flag
  }

  override def equals(other: Any): Boolean = other match {
    case that: Permutation =>  values == that.values
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(values)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"Permutation(${values.mkString(",")})"

  def degree = values.length
}
