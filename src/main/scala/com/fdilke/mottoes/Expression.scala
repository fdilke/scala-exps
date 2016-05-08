package com.fdilke.mottoes

import scala.language.implicitConversions

sealed trait Expression {

}

case class ExpressionOfSort(
  sort: Sort
) extends Expression {

}

object Expression {
  implicit def toExpression(
    x: Symbol
  ): Expression =
    ExpressionOfSort(x)

  implicit def toExpression(
    sort: Sort
  ): Expression =
    ExpressionOfSort(sort)
}
