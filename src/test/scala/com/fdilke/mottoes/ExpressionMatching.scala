package com.fdilke.mottoes

import org.scalatest.Matchers
import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}
import Matchers._

object ExpressionMatching {
  val free =
    new BeMatcher[Expression[_]] {
      override def apply(
        expression: Expression[_]
      ) =
        MatchResult(
          expression.freeVariables.isEmpty,
          "This expression has free variables",
          "This expression has no free variables"
        )
    }

  def haveSort[X](
    sort: Sort[X]
  ) =
      new Matcher[Expression[X]] {
        override def apply(
          expression: Expression[X]
        ) =
          MatchResult(
            expression.sort == sort,
            s"This expression has the wrong sort ${expression.sort} - should be $sort",
            s"This expression should not have the sort $sort"
          )
      }

  def mottoize[X](
    sort: Sort[X]
  ): Matcher[Expression[X]] =
    be(free) and haveSort(sort)
}
