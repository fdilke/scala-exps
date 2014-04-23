package com.fdilke.scala

/**
 * Author: fdilke
 */
object MyApp extends App {
    println("entering MyApp.main()")
    for (arg <- args) {
      println("received arg: " + arg)
    }
}
