package x.y.z

import org.junit.Test

/**
 * Author: fdilke
 */

object SunSpotAnalyser {
  def parse(ints : String) : List[Int] =
    ints.split(" ").map { _.toInt }.toList

  def asString(list : List[Int]) : String =
    list map { _.toString } mkString " "

  def formatTuple(tuple : (Int, Int, Int)) : String =
    tuple match {
      case (m, n, score) => "(" + n + "," + m + " score:" + score + ")"
    }

  def analyseFixedSizeGrid(input : String): String = {
    val list = parse(input)
    val grid = Wrapped2DArray.fromList(5, 5, list)
    asString(grid.scores.toList)
  }

  def analyseVariableSizeGrid(input: String) : String = {
    val list = parse(input)
    val gridSize = list.head
    val gridValues = list.tail
    val grid = Wrapped2DArray.fromList(gridSize, gridSize, gridValues)
    asString(grid.scores.toList)
  }

  def analyseVariableHighScores(input: String) : String = {
    val list = parse(input)
    val numTop = list.head
    val gridSize = list.tail.head
    val gridValues = list.tail.tail
    val grid = Wrapped2DArray.fromList(gridSize, gridSize, gridValues)

    grid.scores.highScores(numTop) map formatTuple mkString ""
  }
}

