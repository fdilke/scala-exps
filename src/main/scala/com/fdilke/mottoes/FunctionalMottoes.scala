package com.fdilke.mottoes

import com.fdilke.mottoes.Sort.flip
import com.fdilke.streams.AllPairs

import scala.Function.tupled
import scala.language.implicitConversions

sealed trait Sort {
  def -:(that: Sort) =
    FunctionSort(that, this)
  def toString: String
  def name: String
  def isBase: Boolean
  def optionallyBracketedName =
    if (isBase)
      toString
    else
      s"($toString)"
  def optionallyFlippedName =
    if (isBase)
      name
    else
      flip(name)
}

case class BaseSort(
  leaf: Symbol
) extends Sort {
  override def isBase = true
  override def toString: String =
    leaf.name
  override def name: String =
    leaf.name
}

case class FunctionSort(
  argSort: Sort,
  returnSort: Sort
) extends Sort {
  override def isBase = false
  override def toString: String =
    argSort.optionallyBracketedName + " => " + returnSort.toString
  override def name: String =
    argSort.optionallyFlippedName + returnSort.name
}

object Sort {
  implicit def asLeaf(x: Symbol): Sort =
    BaseSort(x)

  def flip(text: String): String =
    text map flip

  def flip(ch: Char): Char =
    if (ch.isUpper)
      ch.toLower
    else
      ch.toUpper
}

sealed trait Expression {
  val sort: Sort
  val freeVariables: Seq[Sort]
  val boundVariables: Seq[Sort]

  def >>:(sort: Sort) =
    LambdaExpression(sort, this)
}

case class ExpressionOfSort(
  override val sort: Sort
) extends Expression {
  override val freeVariables = Seq(sort)
  override val boundVariables = Seq()
}

case class LambdaExpression(
  argSort: Sort,
  value: Expression
) extends Expression {
  require(!value.boundVariables.contains(argSort))

  override val sort =
    argSort -: value.sort
  override val freeVariables =
    value.freeVariables.filterNot(_ == argSort)
  override val boundVariables =
    argSort +: value.boundVariables
}

case class FunctionApplicationExpression(
  function: FunctionSort,
  argument: Expression
) extends Expression {
  require(argument.sort == function.argSort)

  override val sort: Sort =
    function.returnSort

  override val freeVariables: Seq[Sort] =
    function +: argument.freeVariables

  override val boundVariables =
    argument.boundVariables
}

object Expressions {
  implicit def asExpression(x : Symbol): Expression =
    ExpressionOfSort(BaseSort(x))

  implicit def asExpression(sort : Sort): Expression =
    ExpressionOfSort(sort)

  implicit class RichSort(
    sort: Sort
  ) {
    def mottoes: Seq[Expression] =
      formulaeGiven()

    def formulaeGiven(
      inputs: Sort*
    ): Seq[Expression] =
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

  implicit class RichFunctionSort(
    fnSort: FunctionSort
  ) {
    def apply(expression: Expression) =
      FunctionApplicationExpression(fnSort, expression)
  }

  def sortOf(x: Symbol) =
    x : Sort

  def allSorts(symbols: Symbol*): Stream[Sort] = {
    val baseSorts: Stream[Sort] = symbols.map(sortOf).toStream

    def all: Stream[Sort] =
      baseSorts #::: AllPairs(all).map {
        tupled {
          (x, y) =>
            x -: y: Sort
        }
      }
    all
  }
}
