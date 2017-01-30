package com.fdilke.scala

object FunWithFunctions extends App {

  object Foo extends (Int => String) {
    override def apply(v1: Int): String = s"Hello $v1"
  }

  val f: Int => String = Foo

  println(s"f(2) = ${f(2)}")

  case class Pair[L, R](left: L, right: R)
  object Bar extends Pair[Int, String](2, "xx") with Function[Boolean, Double] {
    override def apply(v1: Boolean): Double = 3.2
  }

  val bar: Pair[Int, String] = Bar

  println(s"bar = $bar")

  val Pair(l, r) = bar
  println(s"l, r = $l, $r")
  println(s"Bar = $Bar")
  println(s"Bar(true) = ${Bar(true)}")

  val g: (Boolean => Double) with Pair[Int, String] = Bar
  println(s"g(false) = ${g(false)}")

  val d = new Pair[Int, Int](2, 3) with Function[Boolean, Boolean] {
    override def apply(v1: Boolean): Boolean = v1
  }
}
