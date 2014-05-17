package com.fdilke.anita

object AnitaSandbox extends App {
  print("  |")
  for (i <- 1 to 5) {
    print(" " + i + " ")
  }
  println("")
  print("--+-")

  for (i <- 1 to 5) {
    print("---")
  }
  println("")

  for (i <- 1 to 5) {
    print(" " + i + "|")
    for (j <- 1 to 5) {
      print("" + "%2d".format(i * j) + " ")
    }
    println("")
  }
}
