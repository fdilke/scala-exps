package com.fdilke.yasu
import scala.collection.immutable.IndexedSeq

object TheListMap {
  def apply(list: List[Int]): List[List[Int]] =
    list match {
      case Nil => List(List())
      case head :: tail =>
        for {
          y <- apply(list.tail)
          i <- 0 to head
        } yield
          i +: y
    }
}
