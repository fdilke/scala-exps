package com.fdilke.scala

import org.mockito.{IdiomaticMockito, Mockito}
import org.mockito.MockitoSugar._
import org.mockito.VerifyMacro.Once

import scala.language.postfixOps
//import org.mockito.Mockito._
import org.mockito.MockitoScalaSession
import org.mockito.captor.ArgCaptor
import org.scalatest.matchers.should.Matchers._
import org.scalatest.exceptions.TestFailedException
import org.scalatest.funsuite.AnyFunSuite

/**
 * Author: fdilke
 */

object Dummy {
  def myFunc(n : Int) : Int = 2*n + 1
}

class MyScalaTestTests extends AnyFunSuite with IdiomaticMockito {
  test("using ensures") {
    intercept[AssertionError] {
        2 ensuring (_ > 3)
    }
  }

  test("using ===") {
    intercept[TestFailedException] {
      assert(2 === 3)
    }
  }

  test("expect") {
    intercept[TestFailedException] {
      assertResult(2) { 3 }
    }
  }

  test("more ===") {
    val x = 6
    assert(x === 6)
  }

// need ScalaCheck for this:
//  test("try a check-based test") {
//    check( (n : Int) => n > 0 ==> Dummy.myFunc(n) > 0)
//  }

  trait Turtle {
    def penDown(): Unit
    def penUp(): Unit
    def forward(distance : Double): Unit
    def setPosition(m : Double, n : Double): Unit
    def getPosition : (Double, Double)
  }

  def driveTheTurtle(t : Turtle): Unit = {
    t.penUp()
    t.setPosition(10.0,3.0)
    t.forward(5.0)
    assert(t.getPosition === (2,3))
  }

  test("expectations-first style test") {
      val t = mock[Turtle]

      t.getPosition returns ((2.0, 3.0))

      driveTheTurtle(t)

    //    inSequence {
//      inAnyOrder {
      t.penUp() wasCalled Once

//      val captorHorizontal = ArgCaptor[Double]
//      val captorVertical = ArgCaptor[Double]
//      verify(t).setPosition(captorHorizontal, captorVertical)
      t.setPosition(10.0, 3.0) wasCalled Once
//      captorHorizontal hasCaptured 10.0
//      captorVertical hasCaptured 3.0

//      val captorDistance = ArgCaptor[Double]
//      verify(t).forward(captorDistance)
//      captorDistance hasCaptured 5.0
      t.forward(5.0) wasCalled Once
//      }
//    }
  }
}
