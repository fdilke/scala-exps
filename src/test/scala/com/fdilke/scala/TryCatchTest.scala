package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec

import scala.util.{Failure, Success, Try}
import org.scalatest.matchers.should.Matchers._
import org.mockito.{IdiomaticMockito, Mock}

class TryCatchTest extends AnyFunSpec with IdiomaticMockito {
  describe("Try/Catch") {
    it("should return a Success object if there is no problem") {
      Try({ 2 * 3 }) should be(Success(6))
    }

    it("should return a Failure object if something goes wrong") {
      val ex = mock[RuntimeException]
      Try({ throw ex }) should be(Failure(ex))
    }
  }
}
