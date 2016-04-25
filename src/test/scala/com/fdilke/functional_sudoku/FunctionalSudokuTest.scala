package com.fdilke.functional_sudoku

import org.scalatest.Matchers._
import org.scalatest.{FunSpec, Matchers}

import scala.language.implicitConversions
import Node._

class FunctionalSudokuTest extends FunSpec {
  private val Seq(x, y, z) = Seq('x, 'y, 'z)

  describe("The :> operator") {
    it("can be used to build trees") {
      (x : Node[Symbol]) shouldBe LeafNode(x)
      (x :> y) shouldBe BranchNode(LeafNode(x), LeafNode(y))
      x :> (y :> z) shouldBe BranchNode(LeafNode(x), BranchNode(LeafNode(y), LeafNode(z)))
    }
  }
}