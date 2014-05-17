package com.fdilke.anita

object AnitaSandbox extends App {
  val rows = 9
  val columns = 6


  print("  |")
  for (i <- 1 to columns) {
    print(" " + i + " ")
  }
  println("")
  print("--+-")

  for (i <- 1 to columns) {
    print("---")
  }
  println("")

  for (i <- 1 to rows) {
    print(" " + i + "|")
    for (j <- 1 to columns) {
      print("" + "%2d".format(i * j) + " ")
    }
    println("")
  }
}
