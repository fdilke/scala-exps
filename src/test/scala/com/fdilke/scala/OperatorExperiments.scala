package com.fdilke.scala

// User: Felix Date: 05/10/2014 Time: 11:08

object OperatorExperiments extends App {

  case class Foo(i: Int) {
    def apply(that: Foo)(f: Int => Int) = 3
    def >(that: Foo)(f: Int => Int) = 3
    def ::(other: Foo)(f: Int => Int) = 3
    def *(that: Foo)(f: Int => Int) = 3
  }

  def >(one: Foo)(that: Foo)(f: Int => Int) = 3

  println("The experiment was successful: " + {
    val Seq(foo, bar) = Seq(1, 2) map { i => Foo(i) }
    val x  = foo(bar) (_ + 1)
    val y  = (foo > bar) (_ + 1)
    val z  = (foo * bar) (_ + 1)
    val w  = (foo :: bar) (_ + 1)
    w
  })
}
