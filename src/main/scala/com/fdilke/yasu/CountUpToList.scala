package com.fdilke.yasu
import scala.collection.immutable.IndexedSeq

object CountUpToList {

  private def combine(
    counts: List[List[Int]],
    newIndex: Int
  ): List[List[Int]] =
    for {
      count <- counts
      i <- 0 to newIndex
    } yield
      count :+ i

  def apply(
    list: List[Int]
  ): List[List[Int]] =
    list.foldLeft(
      List(List[Int]())
    )(combine)
}
