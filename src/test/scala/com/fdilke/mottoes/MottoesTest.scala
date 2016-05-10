package com.fdilke.mottoes

import com.fdilke.mottoes.Expression._
import com.fdilke.mottoes.ExpressionMatching._
import com.fdilke.mottoes.Sort._
import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class MottoesTest extends FreeSpec {
  private val Seq(a, h, x, y, z, w, s) = Seq('a, 'h, 'x, 'y, 'z, 'w, 's)

  "The -: operator" - {
    "can be used to build compound sorts" in {
      (x: Sort) shouldBe λ()(x)
      (x -: y) shouldBe λ(x)(y)
      x -: y -: z shouldBe λ(x, y)(z)
    }

    "has sane equality semantics" in {
      (x: Expression) should be (x: Expression)
      (x: Expression) should not be (y : Expression)
      x should not be (x -: y)
      (x: Expression) should not be (x -: y: Expression)
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
      val expr = x: Expression
      expr.sort shouldBe sortOf(x)
      expr.freeVariables shouldBe Seq(sortOf(x))
      expr.boundVariables shouldBe empty
    }

    "have sane equality semantics" in {
      (x: Expression) shouldBe (x: Expression)
      (x -: y)(x) should not be (x: Expression)
      (x: Expression) should not be (x -: y)(x)
    }

    "can encode constant functions" in {
      val ky = x >>: y
      ky.sort shouldBe (x -: y)
      ky.freeVariables shouldBe Seq(sortOf(y))
      ky.boundVariables shouldBe Seq(sortOf(x))
    }

    "can encode the K combinator" in {
      val k = x >>: y >>: x
      k.sort shouldBe (x -: y -: x)
      k.freeVariables shouldBe empty
      k.boundVariables shouldBe Seq(sortOf(x), sortOf(y))
    }

    "can encode function application" in {
      val app = (x -: y) (x)
      app.sort shouldBe sortOf(y)
      app.freeVariables shouldBe Seq(x -: y, sortOf(x))
      app.boundVariables shouldBe empty
    }

    "cannot encode a function application with the wrong argument sort" in {
      intercept[IllegalArgumentException] {
        (x -: y) (y)
      }
    }

    "cannot encode a function with repeated argument sorts" in {
      intercept[IllegalArgumentException] {
        x >>: x >>: y
      }
    }

    "can encode a double function application of the right sort" in {
      val doubleApp = (x -: y -: z)(x)(y)
      doubleApp.sort shouldBe sortOf(z)
      doubleApp.freeVariables shouldBe Seq[Sort](x -: y -: z, x, y)
      doubleApp.boundVariables shouldBe Seq()
    }

    "cannot encode a double function application of the wrong sort" in {
      intercept[IllegalArgumentException] {
        (x -: y -: z)(x)(x)
      }
      intercept[IllegalArgumentException] {
        (x -: y -: z)(y)
      }
    }

    "can encode the evaluation sub-motto" in {
      val eval = (x -: y) >>: x >>: (x -: y) (x)
      eval.sort shouldBe (x -: y) -: x -: y
      eval.freeVariables shouldBe empty
      eval.boundVariables shouldBe Seq(x -: y, sortOf(x))
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

      "power continuation algebras" in {
        val xh = x -: h
        val ax = a -: x
        val axh = ax -: h
        val xhh = xh -: h
        val axhh = axh -: h
        val xhhx = xhh -: x
        val axhhax = axhh -: ax
        def em = xhhx >>: axhh >>: a >>: xhhx(
          xh >>: axhh(
            ax >>: xh(ax(a))
          )
        )
        em should mottoize(xhhx -: axhhax)
      }

      "reader monad multiplication" in {
        val sx = s -: x
        val ssx = s -: sx
        val rm = ssx >>: s >>: ssx(s)(s)
        rm should mottoize(ssx -: sx)
      }
    }
  }

  // TODO: continue conversion of tests from old motto code from 'can be queried for mottoes'
}
