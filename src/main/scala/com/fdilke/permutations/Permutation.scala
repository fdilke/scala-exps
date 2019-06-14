package com.fdilke.permutations

import scala.math.max
import scala.language.postfixOps

object Permutation {
  val identity = new Permutation(Seq[Int]())

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
    Permutation(0 until maxDegree map composite:_*)
  }

  def apply(n : Int) : Int = if (n < degree) values(n) else n

  def parity = {
    val array = values.toArray

    var flag = 1
    for (index <- array.indices) {
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

  val degree: Int =
    values.length
}
