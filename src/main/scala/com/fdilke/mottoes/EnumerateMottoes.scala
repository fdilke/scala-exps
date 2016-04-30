package com.fdilke.mottoes

import com.fdilke.mottoes.Expressions._

object EnumerateMottoes extends App {
  private val Seq(a, h, x, y, z, w) = Seq('a, 'h, 'x, 'y, 'z, 'w)

  for (sort <- allSorts(a, h, x).take(100)) {
    val mottoes = sort.mottoes
    if (mottoes.nonEmpty) {
      println(s"$sort\t\t${mottoes.size}")
    }
  }

  // TODO: don't want to count sorts like x => a => x as having mottoes
}
