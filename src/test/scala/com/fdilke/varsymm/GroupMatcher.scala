package com.fdilke.varsymm

import com.fdilke.varsymm.GroupSugar._
import org.scalatest.matchers.{MatchResult, Matcher}

object GroupMatcher {
  def beAGroupOf[T]: Matcher[Group[T]] =
    containTheUnit[T] and
      closedUnderMultiplication[T] and
      closedUnderInversion[T] and
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

  def closedUnderInversion[T]: Matcher[Group[T]] =
    implicit group =>
        MatchResult(
          {
            group.elements.forall{ x =>
              val theInverse = ~x
              group.elements.exists { _ == theInverse }
            }
          },
          "structure is not closed under inversion",
          "structure is closed under inversion"
        )

  def closedUnderMultiplication[T]: Matcher[Group[T]] =
    implicit group =>
        MatchResult(
          {
            group.elements.forall{ x =>
              group.elements.forall { y =>
                val theProduct = x * y
                group.elements.exists {
                  _ == theProduct
                }
              }
            }
          },
          "structure is not closed under inversion",
          "structure is closed under inversion"
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
