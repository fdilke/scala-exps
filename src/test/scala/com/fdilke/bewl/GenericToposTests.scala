package com.fdilke.bewl

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import ShouldMatchers._

trait ToposFixtures[T <: Topos] {
  type FOO
  type BAR
  type BAZ

  val foo : T#DOT[FOO]
  val bar : T#DOT[BAR]
  val baz : T#DOT[BAZ]

  val foo2bar : T#ARROW[FOO, BAR]
  val foo2baz : T#ARROW[FOO, BAZ]
  val foobar2baz : T#BIARROW[FOO, BAR, BAZ]
}

abstract class ToposFixtureSanityTests[T <: Topos](fixtures: ToposFixtures[T]) extends FunSpec {
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

abstract class GenericToposTests[T <: Topos](
    topos: T,
    fixtures: ToposFixtures[T]
  ) extends ToposFixtureSanityTests(fixtures) {

  import topos._
  import fixtures._

  describe(s"The topos ${topos.getClass.getSimpleName}") {
    it("has identity arrows which can be composed") {
      // TODO: get rid of these [FOO]s
      val f2b: T#ARROW[FOO, BAR] = foo2bar
      val i: T#ARROW[FOO, FOO] = foo.identity
//      val f2b_i: topos.type#ARROW[FOO, BAR] = f2b.apply[FOO](i)


      println("the one:   " + i.getClass)
      println("the other: " + f2b.getClass)

//      val f2b_i = f2b.apply[FOO](i)
//
//      println("the one:   " + f2b_i.getClass)
//      println("the other: " + f2b.getClass)

//      f2b_i shouldBe f2b
//
//      foo2bar[FOO](foo.identity) shouldBe foo2bar
//      bar.identity[FOO](foo2bar) shouldBe foo2bar
    }

//    it("can construct biproduct diagrams") {
//      val barXbaz = bar.x[BAZ](baz) // I want to say bar x baz
//
//      val productArrow = barXbaz.multiply[FOO](foo2bar, foo2baz)
//      productArrow.source shouldBe foo
//      productArrow.target shouldBe barXbaz.product
//
//      barXbaz.leftProjection[FOO](productArrow) shouldBe foo2bar
//      barXbaz.rightProjection[FOO](productArrow) shouldBe foo2baz
//    }
//
//    it("has a terminator") {
//      val fooToI = foo.toConstant
//      fooToI.source shouldBe foo
//      fooToI.target shouldBe I
//
//      bar.toConstant[FOO](foo2bar) shouldBe fooToI
//    }
//
//    // Right now, you can't.
////    it("can chain products") {
////      val barXfooXbaz = bar.x[FOO](foo).x[BAZ](baz)
////      val productArrow = barXfooXbaz.multiply(foo2bar, foo.identity, foo2baz)
////      productArrow.source shouldBe foo
////      productArrow.target shouldBe barXfooXbaz.product
////
////      barXfooXbaz.projections(0)(productArrow) shouldBe foo2bar
////      barXfooXbaz.projections(1)(productArrow) shouldBe foo.identity
////      barXfooXbaz.projections(2)(productArrow) shouldBe foo2baz
////    }
//
//    it("can construct exponential diagrams") {
//      val exponential: T#EXPONENTIAL[BAR, BAZ] = baz.^[BAR](bar)
//
//      // Check evaluation maps baz^bar x bar -> baz
//      val ev = exponential.evaluation
//      ev.product.rightProjection.target shouldBe bar
//      ev.arrow.target shouldBe baz
//
//      val _foobar2baz: T#BiArrow[FOO, BAR, BAZ] = foobar2baz
//      val transpose = exponential.transpose[FOO](foobar2baz)
//
//      transpose should have ('source (foo), 'target (ev.product.leftProjection.target))
//
//      // Next, construct the arrow: transpose x 1 : foo x baz -> bar^baz x baz
//      // as the product of foo x baz -> foo -> bar^baz and foo x baz -> baz -> baz
//      val x1 = transpose[(FOO, BAR)](foobar2baz.product.rightProjection)
//      val x2 = foobar2baz.product.rightProjection
//      val t_x_1 = ev.product.multiply(x1, x2)
//      foobar2baz.arrow shouldBe ev.arrow(t_x_1)
//    }
  }
}
