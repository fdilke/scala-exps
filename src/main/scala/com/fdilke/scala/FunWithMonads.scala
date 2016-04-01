package com.fdilke.scala

import scala.languageFeature.higherKinds

object FunWithMonads extends App {

  def mu[X, H]: ((((X => H) => H) => H) => H) => ((X => H) => H) =
    xhhhh => xh => xhhhh(_(xh))

  abstract class Monad[M[X]] {
    def eta[X](x: X): M[X]
    def mu[X](mmx: M[M[X]]): M[X]

    type Algebra[X] = M[X] => X
  }

  class ContextOf[H] {

    type SingleExponential[X] = H => X

    class SingleExponentialMonad extends Monad[SingleExponential] {
      override def eta[X](x: X): SingleExponential[X] =
        h => x

      override def mu[X](
        mmx: SingleExponential[SingleExponential[X]]
      ) =
        h => mmx(h)(h)

      def sectionAlgebra[X](xh: X => H): Algebra[H => X] =
        hhx => h => hhx(h)(h)
    }

    type DoubleExponential[X] = (X => H) => H

    class DoubleExponentialMonad extends Monad[DoubleExponential] {
      override def eta[X](x: X) =
        _(x)

      override def mu[X](
        mmx: DoubleExponential[DoubleExponential[X]]
      ) =
        xh => mmx(_(xh))

      val baseAlgebra: Algebra[H] =
        _({ h => h })
    }
  }
}
