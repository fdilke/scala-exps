package com.fdilke.scala

import org.scalatest.{FunSpec, ShouldMatchers}
import scala.util.{Failure, Success, Try}
import org.scalatest.mock.MockitoSugar.mock

// User: Felix Date: 04/04/2014 Time: 13:48

class TryCatchTest extends FunSpec with ShouldMatchers {
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
