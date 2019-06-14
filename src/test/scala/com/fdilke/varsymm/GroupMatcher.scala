package com.fdilke.varsymm

import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}

object GroupMatcher {
  def groupOf[T]: BeMatcher[Group[T]] =
    group =>
        MatchResult(true,
          "an instance of " + group.getClass.getName,
          "not an instance of " + group.getClass.getName
        )
}
