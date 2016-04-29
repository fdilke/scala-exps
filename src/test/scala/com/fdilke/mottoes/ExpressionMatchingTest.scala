package com.fdilke.mottoes

import com.fdilke.mottoes.ExpressionMatching._
import com.fdilke.mottoes.Expressions._
import com.fdilke.mottoes.Sort._
import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class ExpressionMatchingTest extends FreeSpec {
  "Expression matching works for" - {
    "unbound expressions" in {
      0 >>: 1 should not be free
      0 >>: 0 should be(free)
    }

    "checking the sort" in {
      (0 >>: 1) should haveSort(0 -: 1)
      (0 >>: 1) should not(haveSort(0))
    }

    "checking mottoes" in {
      0 >>: 1 should not(mottoize(0 -: 0))
      0 >>: 0 should not(mottoize(0 -: 1))
      0 >>: 0 should mottoize(0 -: 0)
    }
  }
}
