package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

import scala.language.implicitConversions

class OverrideAbstractTypesTest extends AnyFunSpec {

  describe("Xxx") {
    it("Yyy") {

      trait BaseTopos {
        type ~
        type DOT[~]
        type →[T <: ~, U <: ~] <: ~
        type EXPONENTIAL[S <: ~, T <: ~] =
          ExponentialDot[S, T, S → T] with DOT[S → T]

        trait ExponentialDot[
        S <: ~,
        T <: ~,
        S_T <: S → T
        ] {
          dot: DOT[S_T] =>
        }
      }

      object FiniteSets extends BaseTopos {
        override type ~ = Any
        override type →[T <: ~, U <: ~] = (T => U) with ~

        def xxx[S <: ~, T <: ~]: Unit = {
          val hh: DOT[S → T] with ExponentialDot[S, T, S → T] = ???
          val kk: EXPONENTIAL[S, T] = hh
        }
      }

    }
  }
}