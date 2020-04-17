package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

/*

Road accident, pure and simple
You can't implement an xxxFType{SELF, ...] in two ways

trait CatcherFType[
  SELF <: CatcherFType[SELF, T, U],
  T,
  U
] { self: SELF =>

  def either: Either[
    U,
    T => SELF
  ]
}

class GroundedCatcher[T, U](
 val either: Either[U, T => GroundedCatcher[T, U]]
) extends CatcherFType[GroundedCatcher[T, U], T, U]

sealed trait GroundedTree[T] //
  extends GroundedCatcher[Boolean, T] //
    with CatcherFType[GroundedTree[T], Boolean, T] //

case class LeafNode[T](
  leaf: T
) extends GroundedCatcher[Boolean, T](
  Left(leaf)
) with GroundedTree[T]
*/

class ScalaBugTest extends AnyFunSpec {
  describe("Potential Scala bug") {
    it("can be illustrated") {
//      val xx: GroundedTree[Int] =
//        LeafNode(7)

      // Should be able to do this, but we can't.
//       val yy: CatcherFType[GroundedTree[Int], Boolean, Int] = xx

//      (xx.isInstanceOf[
//        CatcherFType[GroundedTree[Int], Boolean, Int]
//      ]) shouldBe true
//      2 * 2 shouldBe 4
    }
  }
}
