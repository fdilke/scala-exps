package com.fdilke.functional_sudoku

import org.scalatest.{Matchers, FunSpec}
import Matchers._

import Node.Implicits._
import scala.language.implicitConversions

class FunctionalSudokuTest extends FunSpec {
  private val Seq(x, y, z) = Seq('x, 'y, 'z)

  describe("Trees") {
    it("can be built up from tuples") {
      (x : Node[Symbol]) shouldBe LeafNode(x)
      (x -> y : Node[Symbol]) shouldBe BranchNode(LeafNode(x), LeafNode(y))
//      (((x -> (y -> z)) : Node[Symbol]) shouldBe BranchNode(LeafNode(x), LeafNode(y))
    }
  }
}