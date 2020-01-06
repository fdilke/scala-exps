package com.fdilke.oldmottoes

import com.fdilke.oldmottoes.ExpressionMatching._
import com.fdilke.oldmottoes.Expressions._
import com.fdilke.oldmottoes.Sort._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers._

class ExpressionMatchingTest extends AnyFreeSpec {
  private val Seq(x, y) = Seq('x, 'y)

  "Expression matching works for" - {
    "unbound expressions" in {
      x >>: y should not be free
      x >>: x should be(free)
    }

    "checking the sort" in {
      (x >>: y) should haveSort(x -: y)
      (x >>: y) should not(haveSort(x))
    }

    "checking mottoes" in {
      x >>: y should not(mottoize(x -: x))
      x >>: x should not(mottoize(x -: y))
      x >>: x should mottoize(x -: x)
    }
  }
}
