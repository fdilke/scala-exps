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

case class Ratio(
  numerator: Element,
  denominator: Element
)

case class TangentGroup(q: Int) extends Iterable[GroupElement] {
  if (q % 4 != 3) {
    throw new IllegalArgumentException(s"$q is not evenly odd")
  }
  val field =
    FiniteField.GF(q)

  import field._
  val zero =
    groupElement(Some(O))

  val infinity =
    groupElement(None)

  override def iterator: Iterator[GroupElement] =
    (
      field.iterator map { e => groupElement(Some(e)) }
    ) ++ (
      Iterator(groupElement(None))
    )

  def fromRatio(ratio: Ratio) =
    if (ratio.denominator == field.O)
      infinity
    else
      groupElement(
        Some(ratio.numerator / ratio.denominator)
      )

  def groupElement(
    element: Option[Element]
  ) =
    GroupElement(
      this,
      element
    )
}

case class GroupElement(
  group: TangentGroup,
  possiblyFinite: Option[Element]
) {

  import group.field
  import field.{O, I, RichElement}

  def asRatio: Ratio =
    possiblyFinite match {
      case None =>
        Ratio(field.I, field.O)
      case Some(element) =>
        Ratio(element, field.I)
    }

  def unary_- =
    group.groupElement(
      possiblyFinite match {
        case None => None
        case Some(element) => Some(-element)
      }
    )

  def +(other: GroupElement) =
    group.groupElement(
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

