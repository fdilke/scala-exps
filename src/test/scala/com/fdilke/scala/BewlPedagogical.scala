package com.fdilke.scala

import scala.language.higherKinds

object BewlPedagogical {

  trait Category {
    type DOT[S] <: Dot[S]
    type ARROW[S, T] <: Arrow[S, T]

    trait Dot[S] { dot: DOT[S] =>
      val identity: ARROW[S, S]
    }

    trait Arrow[S, T] { arrow: ARROW[S, T] =>
      val source: DOT[S]
      val target: DOT[T]

      def o[R](that: ARROW[R, S]) : ARROW[R, T]
    }
  }
}
