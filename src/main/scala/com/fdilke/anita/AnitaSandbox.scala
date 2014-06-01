package com.fdilke.anita

import com.fdilke.util.FancyMultiplicationImplicits._

object AnitaSandbox extends App {
  def showTable[X](rowRange : Seq[Int], columnRange : Seq[Int], operator:String, f: (Int, Int) => X) {

    val maxLength = (for (i <- rowRange ; j <- columnRange)
      yield f(i, j).toString.length).max

    println("Max Length = " + maxLength)

      print(operator + " |")
    for (i <- columnRange) {
      print(" " + i + " ")
    }
    println("")
    print("--+-")

    for (i <- columnRange) {
      print("---")
    }
    println("")

    for (i <- rowRange) {
      print(" " + i + "|")
      for (j <- columnRange) {
        print("" + "%2s".format(f(i, j)) + " ")
      }
      println("")
    }
    println
  }

//  showTable(1 to 3, 1 to 3, "*", _ * _)
//  showTable(2 to 7, -1 to 8, "+", _ + _)
//  showTable(2 to 7, -1 to 8, "-", _ - _)

  val testRange: Seq[Int] = 1 to 30



  showTable(testRange, testRange, "*", _ * _)
  //showTable(testRange, testRange, "F", _ ** _)

  def different(a:Int, b:Int) =
    if(a*b == a**b)
      " "
    else "*"

//  showTable(testRange,testRange, "D", different)

}

