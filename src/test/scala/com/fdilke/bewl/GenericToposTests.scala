package com.fdilke.bewl

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import ShouldMatchers._

trait ToposFixtures[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] {
  val foo : DOT
  val bar : DOT
  val baz : DOT

  val foo2bar : ARROW
  val foo2baz : ARROW
}

abstract class ToposFixtureSanityTests[
DOT <: ToposDot[DOT, ARROW],
ARROW <: ToposArrow[DOT, ARROW]
](
  fixtures: ToposFixtures[DOT, ARROW]
) extends FunSpec {
  import fixtures._

  describe(s"The fixtures ${fixtures.getClass.getSimpleName}") {
    it("has distinct objects") {
      Set(foo, bar, baz) should have size 3
    }

    it("has arrows whose sources and targets match their names") {
      foo2bar.source shouldBe foo
      foo2bar.target shouldBe bar

      foo2baz.source shouldBe foo
      foo2baz.target shouldBe baz
    }
  }
}

abstract class GenericToposTests[
  DOT <: ToposDot[DOT, ARROW],
  ARROW <: ToposArrow[DOT, ARROW]
  ](
  topos: Topos[DOT, ARROW],
  fixtures: ToposFixtures[DOT, ARROW]
  ) extends ToposFixtureSanityTests(fixtures) {

  import fixtures._
  import topos._

  describe(s"The topos ${topos.getClass.getSimpleName}") {
    it("has identity arrows which can be composed") {
      foo2bar(foo.identity) shouldBe foo2bar
      bar.identity(foo2bar) shouldBe foo2bar
    }

    it("can construct biproduct diagrams") {
      val barXbaz = bar x baz

      val productArrow = barXbaz.multiply(foo2bar, foo2baz)
      productArrow.source shouldBe foo
      productArrow.target shouldBe barXbaz.product

      barXbaz.leftProjection(productArrow) shouldBe foo2bar
      barXbaz.rightProjection(productArrow) shouldBe foo2baz
    }

    it("has a terminator") {
      val fooToI = foo.toConstant
      fooToI.source shouldBe foo
      fooToI.target shouldBe I

      bar.toConstant(foo2bar) shouldBe fooToI
    }

//    it("can chain products") {
//      val barXfooXbar = bar x foo x baz
//      val productArrow = barXfooXbaz.multiply(foo2bar, foo.identity, foo2baz)
//      productArrow.source shouldBe foo
//      productArrow.target shouldBe barXfooXbaz.product
//
//      barXfooXbaz.projections(0)(productArrow) shouldBe foo2bar
//      barXfooXbaz.projections(1)(productArrow) shouldBe foo.identity
//      barXfooXbaz.projections(2)(productArrow) shouldBe foo2baz
//    }
  }
}
