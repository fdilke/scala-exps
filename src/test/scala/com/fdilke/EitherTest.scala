package com.fdilke

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class EitherTest extends AnyFunSpec {
  describe("Eithers") {
    it("can be inherited from Left and Right, then mapped on either side") {
      val left: Either[Int, String] = Left(2)
      val right: Either[Int, String] = Right("x")

      val leftMapped: Either[Int, String] = left.left.map(_ + 1)
      val rightMapped: Either[Int, String] = right.right.map(_ + "y")
      leftMapped shouldBe Left(3)
      rightMapped shouldBe Right("xy")
    }
  }
}
