package com.fdilke.functional_sudoku

import org.scalatest.Matchers._
import org.scalatest.{FunSpec, Matchers}

import scala.language.implicitConversions
import Node._
import Expressions._

class FunctionalMottoesTest extends FunSpec {
  private val Seq(x, y, z) = Seq('x, 'y, 'z)

  describe("The -: operator") {
    it("can be used to build trees") {
      (x: Node[Symbol]) shouldBe LeafNode(x)
      (x -: y) shouldBe BranchNode(LeafNode(x), LeafNode(y))
      x -: (y -: z) shouldBe BranchNode(
        LeafNode(x),
        BranchNode(
          LeafNode(y),
          LeafNode(z)
        )
      )
    }

    it("is not idempotent") {
      x should not be (x -: x)
    }

    it("is not symmetric") {
      (x -: y) should not be (y -: x)
    }

    it("associates to the right and NOT the left") {
      (x -: (y -: z)) should not be ((x -: y) -: z)
      (x -: y -: z) shouldBe (x -: (y -: z))
    }
  }

  describe("Expressions") {
    it("can be formed from a single symbol") {
      val expr = x : Expression[Symbol]
      expr.sort shouldBe (x : Node[Symbol])
      expr.freeVariables shouldBe Seq(x : Node[Symbol])
    }

    it("can be the identity") {
      val id = x >>: x
      id.sort shouldBe (x -: x)
      id.freeVariables shouldBe empty
    }

    it("can be (K y)") {
      val ky = x >>: y
      ky.sort shouldBe (x -: y)
      ky.freeVariables shouldBe Seq(y : Node[Symbol])
    }

    it("can be the combinator K") {
      val k = x >>: y >>: x
      k.sort shouldBe (x -: y -: x)
      k.freeVariables shouldBe empty
    }
  }
}