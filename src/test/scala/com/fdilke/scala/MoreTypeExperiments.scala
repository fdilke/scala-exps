package com.fdilke.scala

// User: Felix Date: 29/05/2014 Time: 19:18

object MoreTypeExperiments {

  trait Terminal

  trait Dot[X,
    DOT[P] <: Dot[P, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    ARROW[P, Q] <: Arrow[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    BIPRODUCT[P, Q] <: Biproduct[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    EXPONENTIAL[P, Q] <: Exponential[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL]
    ] {
    def identity: ARROW[X, X]

    def toConstant: ARROW[X, Terminal]

    def x[Y](that: DOT[Y]): BIPRODUCT[X, Y]

    def ^[Y](that: DOT[Y]): EXPONENTIAL[Y, X]
  }

  trait Arrow[X, Y,
    DOT[P] <: Dot[P, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    ARROW[P, Q] <: Arrow[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    BIPRODUCT[P, Q] <: Biproduct[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    EXPONENTIAL[P, Q] <: Exponential[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL]
  ] {
    val source: DOT[X]
    val target: DOT[Y]

    def apply[W](arrow: ARROW[W, X]): ARROW[W, Y]
  }

  trait Biproduct[X, Y,
    DOT[P] <: Dot[P, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    ARROW[P, Q] <: Arrow[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    BIPRODUCT[P, Q] <: Biproduct[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    EXPONENTIAL[P, Q] <: Exponential[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL]
  ] {
    val leftProjection: ARROW[(X, Y), X]
    val rightProjection: ARROW[(X, Y), Y]

    def multiply[W](leftArrow: ARROW[W, X],
                    rightArrow: ARROW[W, Y]): ARROW[W, (X, Y)]
  }

  trait Exponential[S, T,
    DOT[P] <: Dot[P, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    ARROW[P, Q] <: Arrow[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    BIPRODUCT[P, Q] <: Biproduct[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    EXPONENTIAL[P, Q] <: Exponential[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL]
  ] {
    val evaluation: BiArrow[S => T, S, T, DOT, ARROW, BIPRODUCT, EXPONENTIAL]

    def transpose[W](multiArrow: BiArrow[W, S, T, DOT, ARROW, BIPRODUCT, EXPONENTIAL]): ARROW[W, S => T]
  }

  case class BiArrow[L, R, T,
    DOT[P] <: Dot[P, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    ARROW[P, Q] <: Arrow[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    BIPRODUCT[P, Q] <: Biproduct[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL],
    EXPONENTIAL[P, Q] <: Exponential[P, Q, DOT, ARROW, BIPRODUCT, EXPONENTIAL]
  ](
     product: BIPRODUCT[L, R],
     arrow: ARROW[(L, R), T])
}