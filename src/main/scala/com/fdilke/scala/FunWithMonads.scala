package com.fdilke.scala

import scala.languageFeature.higherKinds

object FunWithMonads extends App {

  def identity[X]: X => X =
    x => x

  def composition[X, Y, Z]:
    (X => Y) => (Y => Z) => (X => Z) =
    xy => yz => x => yz(xy(x))

  def mu[X, H]: ((((X => H) => H) => H) => H) => ((X => H) => H) =
    xhhhh => xh => xhhhh(_(xh))

  trait AContextOf[X, H] {
    type XH  = X => H
    type XHH = XH => H
    type XHHH = XHH => H
    type XHHHH = XHHH => H

    def mu: XHHHH => XHH =
      (xhhhh: XHHHH) => {
        val xhh: XHH =
          (xh: XH) => {
          val xhhh: XHHH =
            (xhh: XHH) => xhh (xh)
          val h:H = xhhhh(xhhh)
          h
        }
        xhh
      }
  }

  // Formula for exponentiating an algebra over the double-exponential monad
  def em[X, H, A]: (
    ((X => H) => H) => X
  ) => (
    (((A => X) => H) => H) => (A => X)
  ) =
    xhhx => axhh => a =>
      xhhx(xh =>
        axhh(ax =>
          xh(ax(a))
        )
      )

  // Strength of the double exponential
  def strength[X, Y, H]: (
      X => Y
    ) => (
      ((X => H) => H) => ((Y => H) => H)
    ) =
    xy => xhh => yh => xhh(x => yh(xy(x)))

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
