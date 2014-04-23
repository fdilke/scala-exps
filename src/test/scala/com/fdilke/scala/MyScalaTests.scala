package com.fdilke.scala

import org.junit._
import Assert._

class MyScalaTests {
  @Test def testSomething() {
    assertEquals(3, 1 + 2)
  }

  @Test def testRecursiveFn() {
    def ack(m:Int,n:Int) : Int =
      if (m == 0)       n+1
      else if (n == 0)  ack(m-1,1)
      else ack(m-1, ack(m, n-1))

    assertEquals(61, ack(3,3))
  }

  @Test def placeholderFunctions() {
    val f = (_ : Int) + (_ : Int)
    assertEquals(4, f(1,3))
  }

  @Test def controlAbstraction() {
    val someStrings = Array("Now", "is",
      "the", "time", "for", "all", "good", "men")
    def filesMatching(query : String,
      matcher: (String, String) => Boolean) = {
        for (string <- someStrings
            if matcher(string, query))
          yield string
    }

    assertTrue(Array("Now", "for", "good")
        sameElements
      filesMatching("o", _.contains(_)))
  }

  @Test def curriedFunctions() {
    def curriedSum(x : Int) (y : Int) = x + y
    assertEquals(7, curriedSum(3)(4))
    assertEquals(List(31,33,35),
      List(1,3,5) map curriedSum(30))
    val inc = curriedSum(1)_
    assertEquals(5, inc(4))
  }

  @Test def controlStructures() {
    def twice(operand : Int) (op : Int => Int) = op(op(operand))
    assertEquals(625, twice(5)(x => x*x))
    assertEquals(625, twice(5) { x => x*x })
  }

  @Test def byNameParameters() {
    def conditionalInvoke(shouldInvoke : Boolean, invocation : => Unit) =
      if (shouldInvoke)
        invocation

    var x = 2
    val (invoke, dont_invoke) = (true, false)
    conditionalInvoke(dont_invoke, x += 1)
    assertEquals(2, x)
    conditionalInvoke(invoke, x += 1)
    assertEquals(3, x)

    conditionalInvoke(dont_invoke, 0 / 0)
  }

  @Test def parameterlessMethods() {
    class Foo(x : Int) {
      def getX = x
    }
    val foo = new Foo(2)
    assertEquals(2, foo getX)
  }

  @Test def simpleTrait() {
    trait Doubling {
      def double(x : Int) = 2 * x
    }
    class Animal
    class Dummy extends Animal with Doubling {
      override def toString = "x2"
    }
    val dummy : Doubling = new Dummy
    assertEquals("x2", dummy toString)
    assertEquals(76, dummy.double(38))
  }

  @Test def abstractTrait() {
    trait Incomplete {
      def mysteryOp(x : Int) : Int
    }
    class Widget extends Incomplete {
      def mysteryOp(x: Int) = x+1
    }
    class Doodad extends Incomplete {
      def mysteryOp(x: Int) = x/5
    }
    val widget : Incomplete = new Widget
    val doodad : Incomplete = new Doodad
    assertEquals(11, widget.mysteryOp(10))
    assertEquals(2, doodad.mysteryOp(10))
  }

  @Test def privateThis() {
    class Dummy (private val text : String) {
      def getText(that : Dummy) : String = that.text
    }
    val d1 = new Dummy("text1")
    val d2 = new Dummy("text2")
    assertEquals("text2", d1.getText(d2))
  }

  @Test(expected = classOf[AssertionError]) def useEnsuring() {
    "" ensuring (_.length > 0)
  }

  @Test(expected = classOf[AssertionError]) def useEnsuring2() {
    def f(n : Int) = {
      n + 1
    } ensuring { _ < 3 }

    f(2)
  }
}

