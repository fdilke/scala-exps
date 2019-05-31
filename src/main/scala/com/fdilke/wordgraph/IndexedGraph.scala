package com.fdilke.wordgraph

import scala.reflect.ClassTag

class IndexedGraph[NODE : ClassTag](
  nodes: Set[NODE],
  rawAdjacency: (NODE, NODE) => Boolean
) {
  private val nodeArray: Array[NODE] =
    nodes.toArray

  val size: Int =
    nodeArray.length

  val adjacencyTable: Array[Array[Boolean]] =
    (0 until size).toArray.map { indexLarger =>
      val nodeLarger = value(indexLarger)
      (0 until indexLarger).toArray.map { indexSmaller =>
        val nodeSmaller = value(indexSmaller)
        rawAdjacency(
          nodeSmaller,
          nodeLarger
        )
      }
  }

  def indexOf(node: NODE): Int =
    nodeArray.indexOf(node)

  def value(index: Int): NODE =
    nodeArray(index)

  def isAdjacent(index1: Int, index2: Int): Boolean =
    index1.compareTo(index2) match {
      case 1 => adjacencyTable(index1)(index2)
      case -1 => adjacencyTable(index2)(index1)
      case _ => false
    }

  def positionFromNodes(
    words: Set[NODE],
    prevWord: Option[NODE]
  ) = Position(
    words map { indexOf },
    prevWord map { indexOf }
  )

  lazy val initialPosition: Position =
    position(
      (0 to size).toSet,
      None
    )

  def position(
    words: Set[Int],
    prevWord: Option[Int]
  ) = Position(
    words,
    prevWord
  )

  case class Position(
    words: Set[Int],
    prevWord: Option[Int]
  ) {
    def allowedMoves: Set[Int] =
      prevWord match {
        case None => words
        case Some(prev) =>
          words.filter {
            isAdjacent(_, prev)
          }
      }

    def allowedMovesAsNodes: Set[NODE] =
      allowedMoves map { value }

    def move(newWord: Int): Position =
      if (!words.contains(newWord))
        throw new IllegalArgumentException("word not in set")
      else if(
        !prevWord.forall {
          isAdjacent(_, newWord)
        }
      ) throw new IllegalArgumentException("word not adjacent to previous")
      else
        position(
          words.filterNot { _ == newWord },
          Some(newWord)
        )

    def moveFromNode(newNode: NODE): Position =
      move(indexOf(newNode))

    def winningMoves: Set[Int] =
      allowedMoves.filter { newWord =>
        move(newWord).isP
      }

    def isP: Boolean =
      winningMoves.isEmpty

    def isN: Boolean =
      !isP
  }
}
