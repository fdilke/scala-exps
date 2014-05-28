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
  val foobar2baz : MultiArrow[DOT, ARROW]
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

    it("can chain products") {
      val barXfooXbaz = bar x foo x baz
      val productArrow = barXfooXbaz.multiply(foo2bar, foo.identity, foo2baz)
      productArrow.source shouldBe foo
      productArrow.target shouldBe barXfooXbaz.product

      barXfooXbaz.projections(0)(productArrow) shouldBe foo2bar
      barXfooXbaz.projections(1)(productArrow) shouldBe foo.identity
      barXfooXbaz.projections(2)(productArrow) shouldBe foo2baz
    }

    it("can construct exponential diagrams") {
      val projections = foobar2baz.product.projections
      projections should have ('size (2))

      val exponential = baz ^ bar

      // Check evaluation maps baz^bar x bar -> baz
      val ev = exponential.evaluation
      val expProjections = ev.product.projections
      expProjections should have ('size (2))
      expProjections(1).target shouldBe bar
      ev.arrow.target shouldBe baz

      val transpose = exponential.transpose(foobar2baz)

      transpose should have ('source (foo), 'target (expProjections(0).target))

      // Next, construct the arrow: transpose x 1 : foo x baz -> bar^baz x baz
      // as the product of foo x baz -> foo -> bar^baz and foo x baz -> baz -> baz
      val x1 = transpose(projections(0))
      val x2 = projections(1)
      val t_x_1 = ev.product.multiply(x1, x2)
      foobar2baz.arrow shouldBe ev.arrow(t_x_1)

// JAVA VERSION:
//      MultiArrow<DOT, ARROW> biArrow = fixtures.arrowFooBarToBaz();
//      List<ARROW> projections = biArrow.getProductDiagram().getProjections();
//      assertTrue(projections.size() == 2);
//      DOT a = projections.get(0).getTarget();
//      DOT b = projections.get(1).getTarget();
//      DOT c = biArrow.getArrow().getTarget();
//      ExponentialDiagram<DOT, ARROW> exponential = _topos.getExponentialDiagram(c, b);
//      // Check evaluation maps C^B x B -> C
//      MultiArrow<DOT, ARROW> ev = exponential.getEvaluation();
//      List<ARROW> expProjections = ev.getProductDiagram().getProjections();
//      assertTrue(expProjections.size() == 2);
//      assertTrue(expProjections.get(1).getTarget() == b);
//      assertTrue(ev.getArrow().getTarget() == c);
//      // and the universal property of evaluation
//      ARROW transpose = exponential.getTranspose(biArrow); // check this maps A -> B^C
//      assertTrue(transpose.getSource() == a);
//      assertTrue(transpose.getTarget() == expProjections.get(0).getTarget()); // the exponent object itself
//      // Next, construct the arrow: transpose x 1 : A x B -> C^B x B as the productGetDiagram of A x B -> A -> C^B and A x B -> B -> B
//      ARROW x1 = transpose.compose(projections.get(0));
//      ARROW x2 = projections.get(1);
//      List<ARROW> multiplicands = new ArrayList<ARROW>();
//      multiplicands.add(x1);
//      multiplicands.add(x2);
//      ARROW t_x_1 = ev.getProductDiagram().multiplyArrows(biArrow.getProductDiagram().getProduct(), multiplicands);
//      assertTrue(biArrow.getArrow().equals(ev.getArrow().compose(t_x_1)));

    }
  }
}
