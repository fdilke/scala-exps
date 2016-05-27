package com.fdilke.mottoes

import com.fdilke.mottoes.Sort.λ
import Sort._

import scala.language.{postfixOps, implicitConversions}

sealed trait Expression {
  val sort: Sort
  val freeVariables: Seq[Sort]
  val boundVariables: Seq[Sort]
  def useCount(sort: Sort): Int

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
  override def useCount(aSort: Sort) =
    if (aSort == sort) 1 else 0
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
  override def useCount(sort: Sort) =
    fn.useCount(sort) + arg.useCount(sort)
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

  override def useCount(sort: Sort) =
    expr.useCount(sort)
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

  implicit def fromSymbol(
    symbol: Symbol
  ): RichSort =
    symbol : Sort

  implicit class RichSort(
    sort: Sort
  ) {
    def mottoes: Seq[Expression] =
      formulaeGiven(Seq())

    def formulaeGiven(
      inputs: Seq[Sort]
    ): Seq[Expression] = {
      println("")
      println("RECURSING: sort: " + sort)
      println("RECURSING: inputs: " + inputs)
      println("RECURSING: sort args: " + sort.args)

      val xx = if (inputs contains sort)
        Seq(ExpressionOfSort(sort))
      else sort.args match {
        case Nil =>
          Seq()

        case (head :: rest) =>
          if (inputs contains head) {
            println(":( Inputs contains head")
            Seq()
          } else
            sort.returns.formulaeGiven(
              inputs :+ head
            ) map { expr =>
              head >>: expr
            }
      }

//      if (sort.args isEmpty)
//        Seq()
//      else
//        sort.returns.formulaeGiven(
//          inputs ++ sort.args
//        ) map { expr =>
//          (sort.args :\ expr) {
//            (x: Sort, y: Expression) =>
//              x >>: y
//          }
//        }

      //          println("Formula found: " + expr)
      //          println("with inputs: " + inputs)
      //          println("and sort args: " + sort.args)

      println("RECURSING: found " + xx.size + " solutions")
      xx
    }
  }

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
