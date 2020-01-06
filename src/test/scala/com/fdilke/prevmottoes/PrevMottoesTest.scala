package com.fdilke.prevmottoes

import Expression._
import ExpressionMatching._
import Sort._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers._

class PrevMottoesTest extends AnyFreeSpec {
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

  "name on sorts" - {
    "works for base sorts" in {
      (x : Sort).name shouldBe "x"
    }
    "works for function sorts" in {
      (x -: y).name shouldBe "xy"
    }
    "works for compound sorts, which are bracketed appropriately" in {
      (x -: y -: z).name shouldBe "xyz"
      ((x -: y) -: z).name shouldBe "XYz"

      (((x -: y) -: z) -: w).name shouldBe "xyZw"
      ((x -: y) -: (z -: w)).name shouldBe "XYzw"
      (x -: ((y -: z) -: w)).name shouldBe "xYZw"
      (x -: (y -: (z -: w))).name shouldBe "xyzw"
      ((x -: (y -: z)) -: w).name shouldBe "XYZw"
    }
  }

  "toString on sorts" - {
    "works for base sorts" in {
      (x : Sort).toString shouldBe "x"
    }
    "works for function sorts" in {
      (x -: y).toString shouldBe "x => y"
    }
    "works for compound sorts, which are bracketed appropriately" in {
      (x -: y -: z).toString shouldBe "x => y => z"
      ((x -: y) -: z).toString shouldBe "(x => y) => z"

      (((x -: y) -: z) -: w).toString shouldBe "((x => y) => z) => w"
      ((x -: y) -: (z -: w)).toString shouldBe "(x => y) => z => w"
      (x -: ((y -: z) -: w)).toString shouldBe "x => (y => z) => w"
      (x -: (y -: (z -: w))).toString shouldBe "x => y => z => w"
      ((x -: (y -: z)) -: w).toString shouldBe "(x => y => z) => w"
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

    "can be queried for usecounts" - {
      "when the usecount is 0" in {
        (x >>: x).useCount(y) shouldBe 0
      }
      "when the usecount is 1" in {
        (x >>: x).useCount(x) shouldBe 1
      }
      "when the usecount is > 1" in {
        ((s -: s-: x) >>: s >>: (s -: s-: x)(s)(s)).useCount(s) shouldBe 2
      }
    }
}

  "can be queried to find formulae given inputs" - {
    "insufficient to calculate the (simple) target" in {
      x.formulaeGiven(Seq(y)) shouldBe Seq.empty
    }

    "insufficient to calculate the (complex) target" in {
      x.formulaeGiven(Seq(y -: y)) shouldBe Seq.empty
    }

    "including the (simple) target" in {
      x.formulaeGiven(Seq(x)) shouldBe Seq[Expression](
        x
      )
    }

    "including the (complex) target for which there are other derivations" in {
      val xx = x -: x
      xx.formulaeGiven(Seq(xx)) shouldBe Seq[Expression](
        xx,
        x >>: x
      )
    }

    "including a (complex) target ... and return an object of correct type" in {
// TODO: fix this
//      val sx = s -: x
//
//      (sx -: s -: x).formulaeGiven(Seq()) shouldBe Seq[Expression](
//        sx >>: x >>: s >>: y
//      )

//      (sx -: s -: x).formulaeGiven(Seq()) shouldBe Seq[Expression](
//        sx >>: x >>: s >>: y
//      )

//      val sx = s -: x
//      s.formulaeGiven(Seq(sx, x)) shouldBe Seq[Expression](
//        sx >>: x >>: s >>: y
//      )
    }
  }

  "can be queried for mottoes" - {
    "when there are none" in {
      checkMottoes(x)
    }

    "when there is only one" in {
      checkMottoes(x -: x,
        x >>: x
      )
    }

    "when there are multiple solutions" in {
      checkMottoes((x -: x) -: (x -: x),
        (x -: x) >>: (x -: x),
        (x -: x) >>: x >>: x
      )
    }

    "when single application is required" in {
      val sx = s -: x
// TODO: fix
//      checkMottoes(sx -: s -: x,
//        sx >>: s >>: sx(s)
//      )
    }
  }

/*
List(
  LambdaExpression(
    s => x,
    ExpressionOfSort(s => x)
  )) was not equal to
Array(
  LambdaExpression(
    s => x,
    LambdaExpression(
      s,
      FunctionApplicationExpression(
        ExpressionOfSort(s => x),
        ExpressionOfSort(s)
    )
  )
))

   */

//         sx >>: sx
//         (x -: x) >>: x >>: (x -: x)(x)

  /*

  // TODO: fix simpler version...

  // TODO: can't quite calculate this itself
  //      "when double application is required" in {
  //        val sx = s -: x
  //        val ssx = s -: sx
  //        checkMottoes(ssx -: sx,
  //          ssx >>: s >>: ssx(s)(s)
  //        )
  //      }
      }

    "We can enumerate sorts" - {
      "of degree 1" in {
        allSorts(x).take(4).toSeq shouldBe Seq[Sort](
          x,
          x -: x,
          x -: (x -: x),
          (x -: x) -: x
        )
      }
      "of degree 2" in {
        allSorts(x, y).take(9).toSeq shouldBe Seq[Sort](
          x,
          y,
          x -: x,
          x -: y,
          y -: x,
          x -: x -: x,
          y -: y,
          (x -: x) -: x,
          x -: x -: y
        )
      }
    }

   */

  private def checkMottoes(
    sort: Sort,
    expectedMottoes: Expression*
  ) {
    for (motto <- expectedMottoes) {
      println("checking motto: " + motto)
      motto should mottoize(sort)
    }
    sort.mottoes shouldBe expectedMottoes
  }
}
