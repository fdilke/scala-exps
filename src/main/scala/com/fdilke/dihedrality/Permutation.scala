package com.fdilke.dihedrality

object Permutation {
  def identity(numSymbols : Int) = new Permutation(Seq.tabulate(numSymbols) { x : Int => x })

  def apply(values:Int*) = new Permutation(values)
}

class Permutation(val values: Seq[Int]) {
  def apply(that : Permutation) = new Permutation(that.values map { values(_) })

//  def at(index: Int) = values(index)

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
}
