package com.fdilke.functional_sudoku

import scala.language.implicitConversions

sealed trait Node[X] {

}

case class BranchNode[X](left: Node[X], right: Node[X]) extends Node[X] {

}

case class LeafNode[X](leaf: X) extends Node[X] {

}

object Node {
  object Implicits {
    implicit def leafAsNode[X](leaf: X): Node[X] =
      LeafNode(leaf)

//    implicit def nodePair[X](x_y: (X, X)): (Node[X], Node[X]) =
//      x_y match {
//        case (x, y) => (x, y)
//      }

    implicit def branchAsNode[X](x_y: (X, X)): Node[X] =
      x_y match {
        case (x, y) => BranchNode(x, y)
      }

    implicit def branchAsNode2[X](x_y: (X, Node[X])): Node[X] =
      x_y match {
        case (x, y) => BranchNode(x, y)
      }
  }
}

class FunctionalSudoku {
}
