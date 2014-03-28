package x.y.z

import org.scalatest.FunSuite
import org.scalatest.exceptions.TestFailedException
import org.scalamock.scalatest.MockFactory

/**
 * Author: fdilke
 */

object Dummy {
  def myFunc(n : Int) : Int = 2*n + 1
}

class MyScalaTestTests extends FunSuite with MockFactory {
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
      expectResult(2) { 3 }
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
    def penDown()
    def penUp()
    def forward(distance : Double)
    def setPosition(m : Double, n : Double)
    def getPosition : (Double, Double)
  }

  def driveTheTurtle(t : Turtle) {
    t.penUp()
    t.setPosition(10.0,3.0)
    t.forward(5.0)
    assert(t.getPosition === (2,3))
  }

  test("expectations-first style test") {
    val t = mock[Turtle]

    inSequence {
      inAnyOrder {
        t.penUp _ expects()
        t.setPosition _ expects(10.0,3.0)
        t.forward _ expects 5.0
        (t.getPosition _ expects()).returning(2.0,3.0)
      }
    }

    driveTheTurtle(t)
  }

  test("record-then-verify style") {
    val t = stub[Turtle]

    t.penUp _ when()
    (t.getPosition _ when()).returns(2.0,3.0)
    (t.setPosition _).when(2.0, 3.0).returns()

    driveTheTurtle(t)
    t.penUp _ verify()
    (t.setPosition _).verify(10.0, 3.0)
    (t.forward _).verify(5.0)
  }
}
