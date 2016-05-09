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

case class FunctionApplicationExpression(
  fn: Expression,
  arg: Expression
) extends Expression {
  override val sort =
    Sort(fn.args.tail, fn.returns)
  override val freeVariables =
    fn.free
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

  implicit class RichFunctionSort(
    sort: Sort
  ) {
    def apply(arg: Sort) =
      if (!sort.args.headOption.contains(arg))
        throw new IllegalArgumentException
      else
        FunctionApplicationExpression(
          sort,
          arg
        )
  }
}
