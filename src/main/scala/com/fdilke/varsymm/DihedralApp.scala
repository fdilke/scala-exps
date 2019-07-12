package com.fdilke.varsymm

import GroupSugar._

object DihedralApp extends App {
  implicit val group = DihedralGroup(12)
  println("Listing elements of order 6")
  for {
    x <- group.elements if group.orderOf(x) == 2
  } {
    val hasSqRoot = group.elements.exists { y =>
      y * y == x
    }
    val hasCubeRoot = group.elements.exists { y =>
      y * y * y == x
    }
    println("element of order 2: " + x +
      (if (hasSqRoot) "²" else "") +
      (if (hasCubeRoot) "³" else "")
    )
  }
}
