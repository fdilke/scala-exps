package x.y.z

import org.junit.{Assert, Test}
import Assert._
import PeanoNum._

/**
 * Author: fdilke
 */
object PeanoNum {
  val ZERO = new PeanoNum {
    def apply[A](f: (A) => A)(a: A) = a
  }
  val ONE = suc(ZERO)
  val TWO = suc(ONE)

  final def suc(n : PeanoNum) : PeanoNum = new PeanoNum {
    def apply[A](f: (A) => A)(a: A): A = f(n(f)(a))
  }

  final def fromInt(n : Int) = new PeanoNum {
    def apply[A](f: (A) => A)(a: A): A = {
      var x = a
      for (i <- 0 until n) {
        x = f(x)
      }
      x
    }
  }
}

trait PeanoNum extends Ordered[PeanoNum] {
  def apply[A](f : A => A)(a : A) : A

  override def equals(that : Any) = that match {
    case that : PeanoNum => (this toInt) == (that toInt)
    case _ => false
  }

  def +(that : PeanoNum) : PeanoNum = this(suc)(that)

  def *(that : PeanoNum) : PeanoNum = this((_ : PeanoNum) + that)(ZERO)

  override def hashCode : Int = toInt

  def toInt : Int = this((x : Int) => x+1)(0)
  def compare(that: PeanoNum): Int = (this toInt) compare (that toInt)
}

class PeanoNumTest {
  @Test def zeroDoesNothing() {
    assertEquals(0, ZERO((x : Int) => x+1)(0))
  }

  @Test def oneDoesItOnce() {
    assertEquals(1, ONE((x : Int) => x+1)(0))
  }

  @Test def twoDoesItTwice() {
    assertEquals(2, TWO((x : Int) => x+1)(0))
  }

  @Test def equality() {
    assertEquals(ZERO, ZERO)
    assertEquals(ONE, ONE)
    assertEquals(TWO, TWO)

    assertEquals(ONE, suc(ZERO))
    assertEquals(TWO, suc(ONE))
    assertEquals(TWO, suc(suc(ZERO)))

    assertNotEquals(ZERO, ONE)
    assertNotEquals(ONE, TWO)
  }

  @Test def ordering() {
    assertTrue(ZERO.isInstanceOf[Ordered[PeanoNum]])

    assertTrue(ZERO < ONE)
    assertTrue(ONE < TWO)

    assertFalse(ZERO > ZERO)
    assertFalse(ZERO > ONE)
    assertFalse(ONE > TWO)
  }

  @Test def conversion() {
    assertEquals(0, ZERO toInt)
    assertEquals(1, ONE toInt)
    assertEquals(2, TWO toInt)

    val thirteen : PeanoNum = fromInt(13)
    assertEquals(13, thirteen toInt)
  }

  @Test def addition() {
    assertEquals(ZERO, ZERO + ZERO)
    assertEquals(ONE, ZERO + ONE)
    assertEquals(ONE, ONE + ZERO)
    assertEquals(TWO, ONE + ONE)
  }

  @Test def multiplication() {
    for (m <- 0 to 9)
      for (n <- 0 to 9) {
        val x = fromInt(m)
        val y = fromInt(n)
        val xy = fromInt(m * n)
        assertEquals(xy, x * y)
      }
  }
}
