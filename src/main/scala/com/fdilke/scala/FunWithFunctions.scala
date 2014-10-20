package com.fdilke.scala

object FunWithFunctions extends App {

  object Foo extends Function[Int, String] {
    override def apply(v1: Int): String = s"Hello $v1"
  }

  val f: Int => String = Foo

  println(s"f(2) = ${f(2)}")

  object Bar extends Tuple2[Int, String](2, "xx") with Function[Boolean, Double] {
    override def apply(v1: Boolean): Double = 3.2
  }

  val bar: (Int, String) = Bar

  println(s"bar = $bar")

  val (l, r) = bar
  println(s"l, r = $l, $r")
  println(s"Bar = $Bar")
  println(s"Bar(true) = ${Bar(true)}")
}
