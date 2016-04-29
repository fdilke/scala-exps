package com.fdilke.mottoes

import scala.language.implicitConversions

sealed trait Node[X] {
  def -:(that: Node[X]) =
    BranchNode(that, this)
}

case class BranchNode[X](
  left: Node[X],
  right: Node[X]
) extends Node[X]

case class LeafNode[X](
  leaf: X
) extends Node[X]

object Node {
  implicit def asLeaf[X](x: X): Node[X] =
    LeafNode(x)
}

sealed trait Expression[X] {
  val sort: Node[X]
  val freeVariables: Seq[Node[X]]

  def >>:(sort: Node[X]) =
    LambdaExpression(sort, this)
}

case class ExpressionOfSort[X](
  override val sort: Node[X]
) extends Expression[X] {
  override val freeVariables = Seq(sort)
}

case class LambdaExpression[X](
  argSort: Node[X],
  value: Expression[X]
) extends Expression[X] {
  override val sort =
    argSort -: value.sort
  override val freeVariables =
    value.freeVariables.filterNot(_ == argSort)
}

case class FunctionApplicationExpression[X](
  function: BranchNode[X],
  argument: Expression[X]
) extends Expression[X] {
  require(argument.sort == function.left)

  override val sort: Node[X] =
    function.right

  override val freeVariables: Seq[Node[X]] =
    function +: argument.freeVariables
}

object Expressions {
  implicit def asExpression[X](x : X): Expression[X] =
    ExpressionOfSort(LeafNode(x))

  implicit def asExpression[X](sort : Node[X]): Expression[X] =
    ExpressionOfSort(sort)

  implicit class RichNode[X](
    node: Node[X]
  ) {
    def mottoes: Seq[Expression[X]] =
      formulaeGiven()

    def formulaeGiven(inputs: Node[X]*): Seq[Expression[X]] =
      (
        if (inputs.contains(node))
          Seq(ExpressionOfSort(node))
        else
          Seq.empty
      ) ++ (
      node match {
        case LeafNode(_) => Seq()
        case BranchNode(arg, fnExp) =>
          fnExp.formulaeGiven(
            inputs :+ arg :_*
          ) map { expr =>
            arg >>: expr
          }
        }
      )
  }

  implicit class RichBranchNode[X](
    branchNode: BranchNode[X]
  ) {
    def apply(expression: Expression[X]) =
      FunctionApplicationExpression(branchNode, expression)
  }

  def sortOf[X](x: X) =
    x : Node[X]
}
