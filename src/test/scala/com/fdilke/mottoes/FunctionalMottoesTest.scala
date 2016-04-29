package com.fdilke.mottoes

import org.scalatest.Matchers._
import org.scalatest.matchers.{MatchResult, Matcher}
import org.scalatest.{FreeSpec, FunSpec, Matchers}

import scala.language.implicitConversions
import Node._
import Expressions._
import ExpressionMatching._

class FunctionalMottoesTest extends FreeSpec {
  private val Seq(h, x, y, z) = Seq('h, 'x, 'y, 'z)

  "The -: operator" - {
    "can be used to build trees" in {
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

    "is not idempotent" in {
      x should not be (x -: x)
    }

    "is not symmetric" in {
      (x -: y) should not be (y -: x)
    }

    "associates to the right and NOT the left" in {
      (x -: (y -: z)) should not be ((x -: y) -: z)
      (x -: y -: z) shouldBe (x -: (y -: z))
    }
  }

  "Expressions" - {
    "can be formed from a single symbol" in {
      val expr = x: Expression[Symbol]
      expr.sort shouldBe sortOf(x)
      expr.freeVariables shouldBe Seq(sortOf(x))
    }

    "can encode constant functions" in {
      val ky = x >>: y
      ky.sort shouldBe (x -: y)
      ky.freeVariables shouldBe Seq(sortOf(y))
    }

    "can encode the K combinator" in {
      val k = x >>: y >>: x
      k.sort shouldBe (x -: y -: x)
      k.freeVariables shouldBe empty
    }

    "can encode function application" in {
      val app = (x -: y) (x)
      app.sort shouldBe sortOf(y)
      app.freeVariables shouldBe Seq(x -: y, sortOf(x))
    }

    "cannot encode an invalid function application" in {
      intercept[IllegalArgumentException] {
        (x -: y) (y)
      }
    }

    "can encode the evaluation sub-motto" in {
      val eval = (x -: y) >>: x >>: (x -: y) (x)
      eval.sort shouldBe (x -: y) -: x -: y
      eval.freeVariables shouldBe empty
    }

    "encode mottoes" - {
      "identity" in {
        val id = x >>: x
        id should mottoize(x -: x)
      }

      "composition" in {
        val o = (x -: y) >>: (y -: z) >>: x >>: (y -: z) ((x -: y) (x))
        o should mottoize((x -: y) -: (y -: z) -: (x -: z))
      }

      "double-exponential multiplication" in {
        val xh = x -: h
        val xhh = xh -: h
        val xhhh = xhh -: h
        val xhhhh = xhhh -: h
        val mu = xhhhh >>: xh >>: xhhhh(xhh >>: xhh(xh))
        mu should mottoize(xhhhh -: xhh)
      }

      "strength of the double exponential" in {
        val xh = x -: h
        val yh = y -: h
        val xy = x -: y
        val xhh = xh -: h
        val yhh = yh -: h
        val strength = xy >>: xhh >>: yh >>: xhh(x >>: yh(xy(x)))
        strength should mottoize(xy -: (xhh -: yhh))
      }
    }
  }
}