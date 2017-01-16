package com.fdilke.finitefields

import scala.util.Try

object TangentGroups extends App {
  0 until 10 map { q =>
    Try {
      val NontrivialPrimePower(p, n) = q
      print(s"$q = $p ^ $n ...")

      val group = new TangentGroup(q)
      println("done")
    }
  }
}

case class TangentGroup(q: Int) {
  if (q % 4 != 3) {
    throw new IllegalArgumentException(s"$q is not evenly odd")
  }
  private val field =
    FiniteField.GF(q)

  import field._
  val zero =
    GroupElement(Some(O))

  val elements =
    new Traversable[GroupElement] {
      override def foreach[U](
        f: GroupElement => U
      ) {
        field.foreach { e =>
          f(GroupElement(Some(e)))
          f(GroupElement(None))
        }
      }
    }

  case class GroupElement(possiblyFinite: Option[Element]) {
    def unary_- = GroupElement(
      possiblyFinite match {
        case None => None
        case Some(element) => Some(-element)
      }
    )

    def +(other: GroupElement) = GroupElement(
      other.possiblyFinite match {
        case None => // infinity, so map a => -1/a
          possiblyFinite match {
            case None => Some(O)
            case Some(element) =>
              conditionalInvert(element)
          }
        case Some(element) =>
          possiblyFinite match {
            case None =>
              conditionalInvert(element)
            case Some(element2) =>
              if (element * element2 == I)
                None
              else
                Some(
                  (element + element2) / (I - element * element2)
                )
          }
      }
    )

    private def conditionalInvert(
      element: Element
    ): Option[Element] =
      if (element == O)
        None
      else
        Some(-(~element))
  }
}
