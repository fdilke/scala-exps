package com.fdilke.bewl

import org.scalatest._
import Matchers._

abstract class ToposWithFixtures {
  type TOPOS <: Topos
  val topos : TOPOS

  type FOO
  type BAR
  type BAZ

  import topos._

  val foo : DOT[FOO]
  val bar : DOT[BAR]
  val baz : DOT[BAZ]

  val foo2bar : ARROW[FOO, BAR]
  val foo2baz : ARROW[FOO, BAZ]
  val foobar2baz : BiArrow[FOO, BAR, BAZ]
}

abstract class ToposFixtureSanityTests[T <: Topos](fixtures: ToposWithFixtures) extends FunSpec {
  import fixtures._

  describe(s"The fixtures for ${fixtures.topos.getClass.getSimpleName}") {
    it("include distinct objects") {
      Set(foo, bar, baz) should have size 3
    }

    it("include arrows whose sources and targets match their names") {
      foo2bar.source shouldBe foo
      foo2bar.target shouldBe bar

      foo2baz.source shouldBe foo
      foo2baz.target shouldBe baz
    }
  }
}

abstract class GenericToposTests[TOPOS <: Topos](
    fixtures: ToposWithFixtures
  ) extends ToposFixtureSanityTests(fixtures) {

  import fixtures._
  import fixtures.topos._

  describe(s"The topos ${topos.getClass.getSimpleName}") {
    it("has identity arrows which can be composed") {
      foo2bar(foo.identity) shouldBe foo2bar
      bar.identity(foo2bar) shouldBe foo2bar
    }

    it("can construct biproduct diagrams") {
      val barXbaz = bar x baz

      val productArrow = foo2bar x foo2baz
      productArrow.source shouldBe foo
      productArrow.target shouldBe barXbaz

      leftProjection(bar, baz)(productArrow) shouldBe foo2bar
      rightProjection(bar, baz)(productArrow) shouldBe foo2baz
    }

    it("has a terminator") {
      val fooToI = foo.toConstant
      fooToI.source shouldBe foo
      fooToI.target shouldBe topos.I

      bar.toConstant(foo2bar) shouldBe fooToI
    }

    it("has standardized products") {
      val product: DOT[(FOO, BAR)] = foo x bar
      product shouldBe (foo x bar)
    }

    it("can chain products") {
      val barXfooXbaz = bar x foo x baz
      val productArrow = foo2bar x foo.identity x foo2baz
      productArrow.source shouldBe foo
      productArrow.target shouldBe barXfooXbaz

      leftProjection(bar, foo, baz)(productArrow) shouldBe foo2bar
      midProjection(bar, foo, baz)(productArrow) shouldBe foo.identity
      rightProjection(bar, foo, baz)(productArrow) shouldBe foo2baz
    }

    it("can construct exponential diagrams") {
      // Check evaluation maps baz^bar x bar -> baz
      val ev = evaluation(bar, baz)
      ev.left shouldBe baz ^ bar
      ev.right shouldBe bar
      ev.arrow.target shouldBe baz

      val tran: ARROW[FOO, BAR => BAZ] = transpose(bar, baz, foobar2baz)

      tran should have('source(foo), 'target(ev.left))

      // Next, construct the arrow: transpose x 1 : foo x baz -> bar^baz x baz
      // as the product of foo x baz -> foo -> bar^baz and foo x baz -> baz -> baz
      foobar2baz.arrow shouldBe ev.arrow(
        tran(leftProjection(foo, bar)) x rightProjection(foo, bar))
    }

    it("has standardized exponentials") {
      val exponential: DOT[BAR => FOO] = foo ^ bar
      exponential shouldBe (foo ^ bar)
    }
  }
}


