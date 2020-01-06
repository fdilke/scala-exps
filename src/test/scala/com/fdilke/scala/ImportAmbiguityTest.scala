package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec

import org.scalatest.matchers.should.Matchers._

class ImportAmbiguityTest extends AnyFunSpec {
  object OneObject {
    def foo(text: String) = true
  }
  object OtherObject {
    def foo(n: Int) = true
  }

  import OneObject._
  import OtherObject.{ foo => bar }

  foo("")
  bar(2)

  type MAPPABLE[_] = (
  { type λ[X] <: Iterable[X] }
  ) # λ[_]

// You wish
//  def inParallel[
//    F <: MAPPABLE,
//    A,
//    B
//  ](fa: F[A], fb: F[B]): F[(A, B)] =
//    for {
//      a <- fa
//      b <- fb
//    }
//      yield (a, b)
}


