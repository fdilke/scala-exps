package com.fdilke.wordgraph

import scala.reflect.ClassTag

case class IndexedGraph[NODE : ClassTag](
  nodes: Set[NODE]
) {
  private val nodeArray: Array[NODE] =
    nodes.toArray

  def indexOf(node: NODE): Int =
    nodeArray.indexOf(node)

  def value(index: Int): NODE =
    nodeArray(index)

  def size(): Int =
    nodeArray.size
}
