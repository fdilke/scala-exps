package com.fdilke.wordgraph

import scala.reflect.ClassTag

case class IndexedGraph[NODE : ClassTag](
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
}
