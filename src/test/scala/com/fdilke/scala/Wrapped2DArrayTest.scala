package com.fdilke.scala

import org.junit.Test
import org.junit.Assert._
import scala.language.postfixOps

class Wrapped2DArrayTest {
  @Test def wrapsArraySafely() {
    val array : Array[Array[Int]] = Array(
      Array(1,2,3),
      Array(4,5,6)
    )
    val wrappedArray = new Wrapped2DArray(array)
    assertEquals(2, wrappedArray(0, 1))
    assertEquals(6, wrappedArray(1, 2))

    assertEquals(0, wrappedArray(-1, 0))
    assertEquals(0, wrappedArray(0, 3))
    assertEquals(0, wrappedArray(2, 0))
    assertEquals(0, wrappedArray(7, 0))
    assertEquals(0, wrappedArray(1, -2))
    assertEquals(0, wrappedArray(1, 18))
  }

  @Test def mapFromList() {
    val inputs = List(5,3,1,2,0,4)
    val wrappedArray : Wrapped2DArray = Wrapped2DArray.fromList(2, 3, inputs)
    assertEquals(3, wrappedArray(0, 1))
    assertEquals(4, wrappedArray(1, 2))
  }

  @Test def mapToList() {
    val inputs = List(5,3,1,2,0,4)
    val wrappedArray : Wrapped2DArray = Wrapped2DArray.fromList(2, 3, inputs)
    assertEquals(inputs, wrappedArray toList)
  }

  @Test def sumWithNeighbours() {
    val inputs = List(5,3,1,
      2,0,4,
      1,1,7)
    val wrappedArray : Wrapped2DArray = Wrapped2DArray.fromList(3, 3, inputs)

    assertEquals(10, wrappedArray.sumWithNeighbours(0,0))
    assertEquals(12, wrappedArray.sumWithNeighbours(1,0))
    assertEquals(15, wrappedArray.sumWithNeighbours(0,1))
  }

  @Test def scores() {
    val inputs = List(
      5,3,1,
      2,0,4,
      1,1,7)
    val expectedScores = List(
      10,15,8,
      12,24,16,
      4,15,12)
    val wrappedArray : Wrapped2DArray = Wrapped2DArray.fromList(3, 3, inputs)
    assertEquals(expectedScores, wrappedArray.scores toList)
  }

  @Test def highScores() {
    val inputs = List(
      5,3,1,
      2,0,4,
      1,1,7)
    val expectedHighValues = List((2,2,7), (0,0,5), (1,2,4))
    val wrappedArray : Wrapped2DArray = Wrapped2DArray.fromList(3, 3, inputs)
    assertEquals(expectedHighValues, wrappedArray.highScores(3))
  }
}

