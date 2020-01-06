package com.fdilke.oldmottoes

import org.scalatest.matchers.should.Matchers._
import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}

object ExpressionMatching {
  val free: BeMatcher[Expression] =
    (expression: Expression) => MatchResult(
      expression.freeVariables.isEmpty,
      "This expression has free variables",
      "This expression has no free variables"
    )

  def haveSort(
    sort: Sort
  ): Matcher[Expression] =
    (expression: Expression) => MatchResult(
      expression.sort == sort,
      s"This expression has the wrong sort ${expression.sort} - should be $sort",
      s"This expression should not have the sort $sort"
    )

  def mottoize(
    sort: Sort
  ): Matcher[Expression] =
    be(free) and haveSort(sort)
}
