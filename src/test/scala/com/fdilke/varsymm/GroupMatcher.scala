package com.fdilke.varsymm

import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}
import GroupSugar._

object GroupMatcher {
//  def combined[T]: BeMatcher[Group[T]] =
//    groupOf[T] compose groupOf[T]

  def beAGroupOf[T]: Matcher[Group[T]] =
    containTheUnit[T] and
      obeyTheUnitLaw[T]

  def containTheUnit[T]: Matcher[Group[T]] =
    group =>
        MatchResult(
          {
            group.elements.exists { _ == group.unit }
          },
          "structure contains its unit",
          "structure does not contain its unit"
        )

  def obeyTheUnitLaw[T]: Matcher[Group[T]] =
    implicit group =>
        MatchResult(
          {
            val unit: T = group.unit
            group.elements.forall { x =>
              ( x * unit == x ) &&
                ( unit * x == x )
            }
          },
          "structure obeys unit law",
          "structure does not obey unit law"
        )
}
