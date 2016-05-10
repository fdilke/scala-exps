package com.fdilke.mottoes

import com.fdilke.mottoes.Sort.λ

import scala.language.implicitConversions

sealed trait Expression {
  val sort: Sort
  val freeVariables: Seq[Sort]
  val boundVariables: Seq[Sort]

  def >>:(arg: Sort) =
    new LambdaExpression(arg, this)

  def apply(arg: Expression) =
    FunctionApplicationExpression(
      this,
      arg
    )
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
  require(
    fn.sort.args.headOption contains arg.sort
  )
  override val sort =
    Sort(fn.sort.args.tail, fn.sort.returns)
  override val freeVariables =
    fn.freeVariables ++ arg.freeVariables
  override val boundVariables =
    fn.boundVariables ++ arg.boundVariables
}

case class LambdaExpression(
  arg: Sort,
  expr: Expression
) extends Expression {
  require(!expr.boundVariables.contains(arg))

  override val sort: Sort =
    λ(arg +: expr.sort.args :_*)(expr.sort.returns)

  override val freeVariables: Seq[Sort] =
    expr.freeVariables.filterNot { _ == arg }

  override val boundVariables: Seq[Sort] =
    arg +: expr.boundVariables
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

//  implicit class RichFunctionSort(
//    sort: Sort
//  ) {
//    def apply(arg: Sort) =
//      if (!sort.args.headOption.contains(arg))
//        throw new IllegalArgumentException
//      else
//        FunctionApplicationExpression(
//          sort,
//          arg
//        )
//  }
}
