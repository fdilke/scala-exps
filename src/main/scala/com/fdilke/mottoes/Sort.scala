package com.fdilke.mottoes

import scala.language.implicitConversions

case class Sort(
  args: Seq[Sort],
  returns: Symbol
) {
  def -: (preArg: Sort) =
    Sort(
      preArg +: args,
      returns
    )
}

object Sort {
  def λ(
    args: Sort*
  ) (
    returns: Symbol
  ): Sort =
    Sort(args, returns)

  implicit def toSort(
    x: Symbol
  ): Sort =
    λ()(x)
}



