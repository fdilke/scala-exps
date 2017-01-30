package com.fdilke.scala

import org.scalatest.FunSpec
import org.scalatest.Matchers._
import org.scalatest.mockito.MockitoSugar.mock
import scala.util.{Failure, Success, Try}

// User: Felix Date: 04/04/2014 Time: 13:48

class TryCatchTest extends FunSpec {
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
