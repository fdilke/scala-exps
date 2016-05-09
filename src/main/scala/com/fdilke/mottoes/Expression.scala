package com.fdilke.mottoes

import scala.language.implicitConversions

sealed trait Expression {
  val sort: Sort
  val freeVariables: Seq[Sort]
  val boundVariables: Seq[Sort]
}

case class ExpressionOfSort(
  override val sort: Sort
) extends Expression {
  override val freeVariables =
    Seq(sort)
  override val boundVariables =
    Seq()
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
