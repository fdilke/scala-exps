package com.fdilke.mottoes

import scala.language.implicitConversions
import Sort._
import Expressions._
import ExpressionMatching._
import scala.language.implicitConversions

object EnumerateMottoes extends App {
  private val Seq(a, h, x, y, z, w) = Seq('a, 'h, 'x, 'y, 'z, 'w)

  for (sort <- allSorts(a, h, x)) {
    val mottoes = sort.mottoes
    if (mottoes.nonEmpty) {
      println(s"$sort\t\t${mottoes.size}")
    }
  }
}
