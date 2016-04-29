package com.fdilke.mottoes

import org.scalatest.Matchers._
import org.scalatest.{FunSpec, Matchers}

import scala.language.implicitConversions
import Node._
import Expressions._

class FunctionalMottoesTest extends FunSpec {
  private val Seq(h, x, y, z) = Seq('h, 'x, 'y, 'z)

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
      expr.sort shouldBe sortOf(x)
      expr.freeVariables shouldBe Seq(sortOf(x))
    }

    it("can encode the identity motto") {
      val id = x >>: x
      id.sort shouldBe (x -: x)
      id.freeVariables shouldBe empty
    }

    it("can encode constant functions") {
      val ky = x >>: y
      ky.sort shouldBe (x -: y)
      ky.freeVariables shouldBe Seq(sortOf(y))
    }

    it("can encode the K combinator") {
      val k = x >>: y >>: x
      k.sort shouldBe (x -: y -: x)
      k.freeVariables shouldBe empty
    }

    it("can encode function application") {
      val app = (x -: y)(x)
      app.sort shouldBe sortOf(y)
      app.freeVariables shouldBe Seq(x -: y, sortOf(x))
    }

    it("cannot encode an invalid function application") {
      intercept[IllegalArgumentException] {
        (x -: y)(y)
      }
    }

    it("can encode evaluation") {
      val eval = (x -: y) >>: x >>: (x -: y)(x)
      eval.sort shouldBe (x -: y) -: x -: y
      eval.freeVariables shouldBe empty
    }

    it("can encode the composition motto") {
      val o = (x -: y) >>: (y -: z) >>: x >>: (y -: z)((x -: y)(x))
      o.freeVariables shouldBe empty
      o.sort shouldBe (x -: y) -: (y -: z) -: (x -: z)
    }

    it("can encode the double-exponential multiplication motto") {
      val xh = x -: h
      val xhh = xh -: h
      val xhhh = xhh -: h
      val xhhhh = xhhh -: h
      val mu = xhhhh >>: xh >>: xhhhh(xhh >>: xhh(xh))
      mu.freeVariables shouldBe empty
      mu.sort shouldBe (xhhhh -: xhh)
    }
  }
}