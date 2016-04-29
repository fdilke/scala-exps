package com.fdilke.mottoes

import scala.language.implicitConversions

sealed trait Sort[X] {
  def -:(that: Sort[X]) =
    FunctionSort(that, this)
}

case class FunctionSort[X](
  argSort: Sort[X],
  returnSort: Sort[X]
) extends Sort[X]

case class BaseSort[X](
  leaf: X
) extends Sort[X]

object Sort {
  implicit def asLeaf[X](x: X): Sort[X] =
    BaseSort(x)
}

sealed trait Expression[X] {
  val sort: Sort[X]
  val freeVariables: Seq[Sort[X]]
  val boundVariables: Seq[Sort[X]]

  def >>:(sort: Sort[X]) =
    LambdaExpression(sort, this)
}

case class ExpressionOfSort[X](
  override val sort: Sort[X]
) extends Expression[X] {
  override val freeVariables = Seq(sort)
  override val boundVariables = Seq()
}

case class LambdaExpression[X](
  argSort: Sort[X],
  value: Expression[X]
) extends Expression[X] {
  require(!value.boundVariables.contains(argSort))

  override val sort =
    argSort -: value.sort
  override val freeVariables =
    value.freeVariables.filterNot(_ == argSort)
  override val boundVariables =
    argSort +: value.boundVariables
}

case class FunctionApplicationExpression[X](
  function: FunctionSort[X],
  argument: Expression[X]
) extends Expression[X] {
  require(argument.sort == function.argSort)

  override val sort: Sort[X] =
    function.returnSort

  override val freeVariables: Seq[Sort[X]] =
    function +: argument.freeVariables

  override val boundVariables =
    argument.boundVariables
}

object Expressions {
  implicit def asExpression[X](x : X): Expression[X] =
    ExpressionOfSort(BaseSort(x))

  implicit def asExpression[X](sort : Sort[X]): Expression[X] =
    ExpressionOfSort(sort)

  implicit class RichSort[X](
    sort: Sort[X]
  ) {
    def mottoes: Seq[Expression[X]] =
      formulaeGiven()

    def formulaeGiven(
      inputs: Sort[X]*
    ): Seq[Expression[X]] =
      (
        if (inputs.contains(sort))
          Seq(ExpressionOfSort(sort))
        else
          Seq()
      ) ++ (
      sort match {
        case BaseSort(_) => Seq()
        case FunctionSort(arg, fnExp) =>
          fnExp.formulaeGiven(
            inputs :+ arg :_*
          ) map {
            arg >>: _
          }
        }
      )
  }

  implicit class RichFunctionSort[X](
    fnSort: FunctionSort[X]
  ) {
    def apply(expression: Expression[X]) =
      FunctionApplicationExpression(fnSort, expression)
  }

  def sortOf[X](x: X) =
    x : Sort[X]
}
