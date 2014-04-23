package com.fdilke.scala

/**
 * Author: fdilke
 */

object Wrapped2DArray {
  def fromList (height : Int, width : Int, list : List[Int]) : Wrapped2DArray = {
    new Wrapped2DArray(Array.tabulate(height, width)
      { (m , n) => list(m * width + n) })
  }
}

class Wrapped2DArray (array : Array[Array[Int]]) {
  def apply(m : Int, n : Int) : Int =
    if (m >= 0 && m < array.length &&
        n >= 0 && n < array(m).length)
      array(m)(n)
    else 0

  def sumWithNeighbours(m : Int, n : Int) : Int =
    (for {i <- m-1 to m+1
          j <- n-1 to n+1}
          yield this(i, j)
    ).sum

  def scores = new Wrapped2DArray(
    Array.tabulate(array.length, array(0).length)(sumWithNeighbours)
  )

  def toList = {
    (for {row   <- array
          value <- row}
        yield value
    ).toList
  }

  def highScores(t : Int) : IndexedSeq[(Int, Int, Int)] = {
    val allScores : IndexedSeq[(Int, Int, Int)] = for {
      m <- 0 until array.length
      n <- 0 until array(m).length }
          yield (m, n, array(m)(n))
     allScores sortBy { -_._3 } take t
  }
}

