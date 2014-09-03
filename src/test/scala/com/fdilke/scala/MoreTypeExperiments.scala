package com.fdilke.scala

import org.scalatest.FunSpec
import scala.language.higherKinds

// User: Felix Date: 29/05/2014 Time: 19:18

object MoreTypeExperiments {

  trait Topos {
    type DOT[P] <: Dot[P]
    type ARROW[P, Q] <: Arrow[P, Q]

    trait Dot[X] {
      def identity: ARROW[X, X]
    }

    trait Arrow[X, Y] {
      def apply[W](arrow: ARROW[W, X]): ARROW[W, Y]
    }
  }

  trait ToposFixtures[T <: Topos] {
    type FOO
    type BAR

    val foo : T#DOT[FOO]
    val bar : T#DOT[BAR]

    val foo2bar : T#ARROW[FOO, BAR]
  }

  abstract class SimplifiedToposTests[
      T <: Topos,
      F <: ToposFixtures[T]
    ](topos: T, fixtures: F) extends FunSpec { self =>

    describe(s"The topos ${topos.getClass.getSimpleName}") {
      it("has identity arrows which can be composed") {
// result is confusion about what all these types are
//        val f2b: T#ARROW[F#FOO, F#BAR] = fixtures.foo2bar
//        val f2b: T#ARROW[SimplifiedToposTests.this.type#fixtures#FOO, SimplifiedToposTests.this.type#fixtures#BAR] = fixtures.foo2bar
//
//        val i = fixtures.foo.identity
//        f2b.apply[fixtures.type#FOO](i)
      }
    }
  }
}