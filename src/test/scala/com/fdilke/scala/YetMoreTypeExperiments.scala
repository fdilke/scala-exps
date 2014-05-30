package com.fdilke.scala

object YetMoreTypeExperiments {

  trait HasFoo {
    type FOO
    val foo: FOO
//    def funWithFoo(foo: FOO)
  }

  class SampleHasFoo {
    type FOO = String
    val foo:FOO = "H"
//    def funWithFoo(foo: FOO) {
//      println("the foo is: " + foo)
//    }
  }

  class HasFooTest[F <: HasFoo](val fixture: F) extends App {
// Why can't we refer to this type properly? There must be a name for it.
    val x: SampleHasFoo#FOO = null
//      val f: F#FOO = fixture.foo
  }
}
