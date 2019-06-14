package com.fdilke.varsymm

import com.fdilke.varsymm.GroupSugar._
import org.scalatest.matchers.{MatchResult, Matcher}

object GroupMatcher {
  def beAGroupOf[T]: Matcher[Group[T]] =
    containTheUnit[T] and
      obeyTheUnitLaw[T] and
      isAssociative[T] and
      hasInverses[T]

  def containTheUnit[T]: Matcher[Group[T]] =
    group =>
        MatchResult(
          {
            group.elements.exists { _ == group.unit }
          },
          "structure does not contain its unit",
          "structure contains its unit"
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
          "structure does not obey unit law",
          "structure obeys unit law"
        )

  def isAssociative[T]: Matcher[Group[T]] =
    implicit group =>
        MatchResult(
          {
            group.elements.forall { x =>
              group.elements.forall { y =>
                group.elements.forall { z =>
                  (x * y) * z == x * (y * z)
                }
              }
            }
          },
          "structure is not associative",
          "structure is associative"
        )

  def hasInverses[T]: Matcher[Group[T]] =
    implicit group =>
        MatchResult(
          {
            group.elements.forall { x =>
              (x * ~x == group.unit) &&
              (~x * x == group.unit)
            }
          },
          "structure does not have inverses",
          "structure has inverses"
        )
}
