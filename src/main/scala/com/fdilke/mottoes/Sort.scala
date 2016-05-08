package com.fdilke.mottoes

import scala.language.implicitConversions

case class Sort(
  args: Sort*
) (
  returns: Symbol
) {
  def -: (preArg: Sort) =
    Sort(
      preArg +: args :_*
    ) (
      returns
    )
}

object Sort {
  implicit def toSort(x: Symbol): Sort =
    Sort()(x)
}



