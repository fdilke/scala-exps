package com.fdilke.apps

object LittleApp extends App {
  val input: List[Int] = List(1, 2, 4, 3)

  def permutations[T](list: List[T]): List[List[T]] =
    list match {
      case Nil => List(List())
      case head :: tail =>
        permutations(tail) map { permutation: List[T] =>
          permutation
        }
    }

  // feel free to cannibalize
}
