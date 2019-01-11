package com.fdilke.prevmottoes

import com.fdilke.prevmottoes.Sort.flip

import scala.language.{postfixOps, implicitConversions}

case class Sort(
  args: Seq[Sort],
  returns: Symbol
) {
  def -: (preArg: Sort) =
    Sort(
      preArg +: args,
      returns
    )

  def simple: Boolean =
    args isEmpty

  def name: String =
    if (simple)
      returns.name
    else
      args.head.altName +
        Sort(args.tail, returns).name

  private def altName: String =
    if (simple)
      name
    else
      flip(name)

  override def toString: String =
    if (simple)
      name
    else
      args.head.altToString + " => " +
        Sort(args.tail, returns).toString

  private def altToString: String =
    if (simple)
      toString
    else
      "(" + toString + ")"
}

object Sort {
  def λ(
    args: Sort*
  ) (
    returns: Symbol
  ): Sort =
    Sort(args, returns)

  implicit def sortOf(
    x: Symbol
  ): Sort =
    λ()(x)

  def flip(text: String): String =
    text map flip

  def flip(ch: Char): Char =
    if (ch.isUpper)
      ch.toLower
    else
      ch.toUpper
}



