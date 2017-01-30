package com.fdilke.scala

import scala.language.higherKinds

object FunWithMonads extends App {

  def identity[X]: X => X =
    x => x

  def composition[X, Y, Z]:
    (X => Y) => (Y => Z) => (X => Z) =
    xy => yz => x => yz(xy(x))

  def mu[X, H]: ((((X => H) => H) => H) => H) => ((X => H) => H) =
    xhhhh => xh => xhhhh(_(xh))

  // Strength of the double exponential
  def strength[X, Y, H]: (
    X => Y
    ) => (
    ((X => H) => H) => ((Y => H) => H)
    ) =
    xy => xhh => yh => xhh(x => yh(xy(x)))

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

  // Applicative strength of continuation (aka double-exponential)
  def as[H, A, B]: (
    ((A => B) => H) => H
  ) => (
    (A => H) => H
  ) => (
    (B => H) => H
  ) =
    abhh => ahh => bh =>
      abhh(ab =>
        ahh(a =>
          bh(ab(a))
        )
      )

  // Multiplication for the reader monad
  def rm[X, S]: (S => S => X) => S => X =
    ssx => s => ssx(s)(s)

  abstract class Monad[M[X]] {
    def eta[X](x: X): M[X]
    def mu[X](mmx: M[M[X]]): M[X]

    type Algebra[X] = M[X] => X
  }

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
