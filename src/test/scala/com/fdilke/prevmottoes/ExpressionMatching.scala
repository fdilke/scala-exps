package com.fdilke.prevmottoes

import org.scalatest.Matchers
import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}
import Matchers._

object ExpressionMatching {
  val free =
    new BeMatcher[Expression] {
      override def apply(
        expression: Expression
      ) =
        MatchResult(
          expression.freeVariables.isEmpty,
          "This expression has free variables",
          "This expression has no free variables"
        )
    }

  def haveSort(
    sort: Sort
  ) =
    new Matcher[Expression] {
      override def apply(
        expression: Expression
      ) =
        MatchResult(
          expression.sort == sort,
          s"This expression has the wrong sort ${expression.sort} - should be $sort",
          s"This expression should not have the sort $sort"
        )
    }

  def mottoize(
    sort: Sort
  ): Matcher[Expression] =
    be(free) and haveSort(sort)
}
