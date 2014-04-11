package com.fdilke.bewl

import org.scalatest._

trait ToposFixtures[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] {
  val foo : DOT
  val bar : DOT
  val foo2bar : ARROW
}

abstract class GenericToposTests[
  DOT <: ToposDot[DOT, ARROW],
  ARROW <: ToposArrow[DOT, ARROW]
  ](
  topos: Topos[DOT, ARROW],
  fixtures: ToposFixtures[DOT, ARROW]
  ) extends FunSpec with ShouldMatchers {

  import fixtures._

  describe(s"The topos ${topos.getClass.getSimpleName}") {
    it("should have identity arrows which can be composed") {
      foo2bar(foo.identity) should be(foo2bar)
      bar.identity(foo2bar) should be(foo2bar)
    }

    it("should be able to construct product diagrams") {

    }
  }
}
