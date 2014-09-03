package com.fdilke.fancycode

import scala.language.higherKinds

// Proof-of-concept for 'Strong Binding'

object StrongBinding {
  trait Category {
    type ELEMENT[X] <: Element[ELEMENT[X]]
    type DOT[X <: Element[X]] <: Dot[X]
    type ARROW[X <: Element[X], Y <: Element[Y]] <: Arrow[X, Y]

    trait Dot[X <: Element[X]] {
      def identity: Arrow[X, X]
    }
    trait Arrow[X <: Element[X], Y <: Element[Y]] {
      def apply[W <: Element[W]](arrow: ARROW[W, X]): ARROW[W, Y]
      def wrap: X
    }
    trait Element[X <: Element[X]] {
      def arrowFrom[W <: Element[W]](dot: DOT[W]): ARROW[W, X]
    }
  }

  class FiniteSets extends Category {
    type DOT[X <: Element[X]] = FiniteSetsDot[X]
    type ARROW[X <: Element[X], Y <: Element[Y]] = WrappedArrow[X, Y]

    class FiniteSetsDot[X <: Element[X]] extends Dot[X] {
      override def identity: Arrow[X, X] = ???
    }

    class WrappedArrow[X <: Element[X], Y <: Element[Y]] extends Arrow[X, Y] {
      override def apply[W <: Element[W]](arrow: ARROW[W, X]): ARROW[W, Y] = ???

      override def wrap: X = ???
    }

    // Below this line we don't assume type parameters are elements

    class FiniteSetsElement[X] extends Element[FiniteSetsElement[X]] {
      override def arrowFrom[W <: Element[W]](dot: DOT[W]): ARROW[W, FiniteSetsElement[X]] = ???
    }

    class WrappedSet[X](elements: Traversable[X]) extends FiniteSetsDot[FiniteSetsElement[X]]
    }
}
