package com.fdilke.scala

// User: Felix Date: 29/05/2014 Time: 19:18

object MoreTypeExperiments {

  trait Topos[
    DOT[P] <: Dot[P],
    ARROW[P, Q] <: Arrow[P, Q],
    BIPRODUCT[P, Q] <: Biproduct[P, Q],
    EXPONENTIAL[P, Q] <: Exponential[P, Q]
  ] {

    trait Terminal

    trait Dot[X
    ] {
      def identity: ARROW[X, X]

      def toConstant: ARROW[X, Terminal]

      def x[Y](that: DOT[Y]): BIPRODUCT[X, Y]

      def ^[Y](that: DOT[Y]): EXPONENTIAL[Y, X]
    }

    trait Arrow[X, Y
    ] {
      val source: DOT[X]
      val target: DOT[Y]

      def apply[W](arrow: ARROW[W, X]): ARROW[W, Y]
    }

    trait Biproduct[X, Y
    ] {
      val leftProjection: ARROW[(X, Y), X]
      val rightProjection: ARROW[(X, Y), Y]

      def multiply[W](leftArrow: ARROW[W, X],
                      rightArrow: ARROW[W, Y]): ARROW[W, (X, Y)]
    }

    trait Exponential[S, T
    ] {
      val evaluation: BiArrow[S => T, S, T]

      def transpose[W](multiArrow: BiArrow[W, S, T]): ARROW[W, S => T]
    }

    case class BiArrow[L, R, T
    ](
       product: BIPRODUCT[L, R],
       arrow: ARROW[(L, R), T])

  }
}