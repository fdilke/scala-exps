package com.fdilke.scala

import org.scalatest.FunSuite
import org.junit.Assert
import Assert._
import Titanic._
import scala.collection.mutable

/**
 * Author: fdilke
 */

class Grid(gridSize: Int) {

  class Line(p: Point, q: Point) {
    require(p != q)
    val points = buildPoints(p, q)

    private def buildPoints(p: Point, q: Point): Set[Point] = {
      var diff = (q - p).unit
      val buffer = mutable.ListBuffer[Point]()
      var r = p
      while (contains(r)) {
        buffer += r
        r += diff
      }
      r = p - diff
      while (contains(r)) {
        buffer += r
        r -= diff
      }
      buffer.toSet
    }

    def size: Int = points.size

    override def equals(that: Any) = that match {
      case that: Line => points == that.points
      case _ => false
    }

    override def hashCode: Int = points.hashCode()

    override def toString = points.toString()
  }

  def line(p: Point, q: Point) = new Line(p, q)

  def enumeratePointSets(f: Set[Point] => Unit) {
    enumerateSubsets(points toSet) {
      f
    }
  }

  lazy val points =
    for {i <- 0 to gridSize
         j <- 0 to gridSize
    } yield Point(i, j)

  def pointSets: Set[Set[Point]] = {
    val buffer = new mutable.ListBuffer[Set[Point]]
    enumeratePointSets {
      subset => buffer += subset
    }
    buffer.toSet
  }

  def contains: (Point => Boolean) = {
    case Point(x, y) => x >= 0 && x <= gridSize &&
      y >= 0 && y <= gridSize
  }

  def countLines: Int = lines.size

  val lines: Set[Grid#Line] = {
    val set = mutable.Set[Grid#Line]()
    for {p <- points
         q <- points
         if p != q
    } {
      set += line(p, q)
    }
    Set.empty ++ set
  }

  val mapLines: Map[Int, Int] = {
    val map = mutable.Map[Int, Int]()
    for (line <- lines) {
      val size = line.size
      map(size) = map.get(size) match {
        case Some(x: Int) => x + 1
        case None => 1
      }
    }
    Map.empty ++ map
  }
}

object Point {
  def areMultiples(p: Point, q: Point): Boolean = (p.x * q.y) == p.y * q.x

  def collinear(p: Point, q: Point, r: Point): Boolean = areMultiples(q - p, r - p)
}

case class Point(x: Int, y: Int) {
  // the 'fundamental unit' of a Point regarded as a vector: divide through by the gcd
  def unit: Point = {
    val d = gcd(x.abs, y.abs)
    Point(x / d, y / d)
  }

  override def equals(that: Any) = that match {
    case p: Point => x == p.x && y == p.y
    case _ => false
  }

  override def hashCode = x + 17 * y

  def -(that: Point) = Point(x - that.x, y - that.y)

  def +(that: Point) = Point(x + that.x, y + that.y)
}

object Titanic {
  def isTitanic(set: Set[Point]): Boolean = {
    set.exists {
      x =>
        set.exists {
          y =>
            x != y &&
              !set.exists {
                z =>
                  x != z && y != z && Point.collinear(x, y, z)
              }
        }
    }
  }

  def T(N: Int) = {
    var sum = 0
    new Grid(N) enumeratePointSets {
      set =>
        if (Titanic.isTitanic(set)) sum += 1
    }
    sum
  }

  def pow(n: Int, exp: Int): BigInt =
    List.fill(exp)(n)./:(BigInt(1)) {
      _ * _
    }

  def T2(N: Int): BigInt = {
    val points = (N + 1) * (N + 1)
    val totalPointSets = pow(2, points)
    val singletons = points
    val doubletons = points * (points - 1) / 2

    val collinears = (
      for ((size, count) <- new Grid(N).mapLines)
      yield count * (pow(2, size) - 1 - size)
      )./:(BigInt(0)) {
      _ + _
    } + 1 + singletons

    totalPointSets - collinears + doubletons
  }

  def enumerateSubsets(set: Set[Point])(f: Set[Point] => Unit) {
    set.toList match {
      case Nil => f(Set.empty)
      case (x :: rest) => enumerateSubsets(rest toSet) {
        s =>
          f(s)
          f(s union Set(x))
      }
    }
  }

  def subsets(set: Set[Point]): Set[Set[Point]] = {
    val buffer = new mutable.ListBuffer[Set[Point]]
    enumerateSubsets(set) {
      subset => buffer += subset
    }
    buffer.toSet
  }

  def main(args: Array[String]) {
    for (n <- 0 to 10000) {
      print("T(" + n + ") = ")
      val now = System.currentTimeMillis()
      print(Titanic.T2(n))
      val then = System.currentTimeMillis()
      val timeDiff = then - now
      println("\t\t" + timeDiff + "msec")
    }
  }

  def gcd(m: Int, n: Int): Int =
    if (m < n) gcd(n, m)
    else if (m == 0) 1
    else if (n == 0) m
    else gcd(m - n, n)
}

class TitanicTest extends FunSuite {

  test("gcd") {
    assert(gcd(0, 0) === 1)
    assert(gcd(0, 2) === 2)
    assert(gcd(2, 0) === 2)
    assert(gcd(2, 1) === 1)
    assert(gcd(2, 6) === 2)
    assert(gcd(4, 6) === 2)
    assert(gcd(6, 9) === 3)
    assert(gcd(12, 8) === 4)
  }

  test("point equality") {
    val p = Point(0, 0)
    val q = Point(0, 1)
    val r = Point(0, 1)
    assert(p === p)
    assertFalse(p == q)
    assert(q.equals(r))
  }

  test("subsets of the empty set") {
    assertResult(Set(Set.empty)) {
      Titanic.subsets(Set.empty)
    }
  }

  test("subsets of a singleton") {
    val p = Point(0, 0)
    assertResult(Set(Set.empty, Set(p))) {
      Titanic.subsets(Set(p))
    }
  }

  test("subsets of a doubleton") {
    val p = Point(0, 0)
    val q = Point(0, 1)
    assertResult(Set(Set.empty, Set(p), Set(q), Set(p, q))) {
      Titanic.subsets(Set(p, q))
    }
  }

  test("tuples on a grid of size 0") {
    assertResult(Set(Set.empty, Set(Point(0, 0)))) {
      new Grid(0).pointSets
    }
  }

  test("linear dependence of points") {
    assertTrue(Point.areMultiples(Point(1, 3), Point(2, 6)))
    assertFalse(Point.areMultiples(Point(1, 3), Point(2, 7)))
  }

  test("collinearity of points") {
    val p = Point(0, 1)
    val q = Point(0, 2)
    val r = Point(0, 3)
    val s = Point(1, 1)
    assertTrue(Point.collinear(p, p, p))
    assertTrue(Point.collinear(p, q, r))
    assertFalse(Point.collinear(p, q, s))
  }

  test("the empty set is not titanic") {
    assertFalse(Titanic.isTitanic(Set.empty))
  }

  test("a two point set is titanic") {
    assertTrue(Titanic.isTitanic(Set(
      Point(0, 0),
      Point(0, 1)
    )))
  }

  test("a collinear three point set is not titanic") {
    assertFalse(Titanic.isTitanic(Set(
      Point(0, 0),
      Point(0, 1),
      Point(0, 2)
    )))
  }

  test("a noncollinear three point set is titanic") {
    assertTrue(Titanic.isTitanic(Set(
      Point(0, 0),
      Point(0, 1),
      Point(1, 2)
    )))
  }

  test("point arithmetic") {
    val p = Point(2, 3)
    val q = Point(8, 7)

    assert(p - q === Point(-6, -4))
    assert(p + q === Point(10, 10))
  }

  test("the fundamental unit of a point") {
    assert(Point(0, 1) === Point(0, 3).unit)
    assert(Point(1, 0) === Point(3, 0).unit)
    assert(Point(0, -1) === Point(0, -3).unit)
    assert(Point(-1, 0) === Point(-3, 0).unit)
    assert(Point(-2, 3) === Point(-4, 6).unit)
  }

  test("when is a point on a grid") {
    val grid = new Grid(4)

    assertTrue(grid contains Point(0, 0))
    assertTrue(grid contains Point(1, 3))
    assertTrue(grid contains Point(4, 1))
    assertTrue(grid contains Point(1, 4))

    assertFalse(grid contains Point(-1, 2))
    assertFalse(grid contains Point(5, 2))
    assertFalse(grid contains Point(3, -1))
    assertFalse(grid contains Point(3, 5))
  }

  test("lines: sanity checking") {
    val grid = new Grid(3)
    intercept[IllegalArgumentException] {
      grid.line(Point(0, 0), Point(0, 0))
    }
  }

  test("lines: as point sets") {
    val grid = new Grid(2)
    val line = grid.line(Point(0, 0), Point(2, 2))
    assert(line.points == Set(
      Point(0, 0), Point(1, 1), Point(2, 2)
    ))
  }

  test("lines: sizing") {
    val grid = new Grid(3)
    val l = grid.line(Point(0, 0), Point(1, 1))
    assert(4 === l.size)

    val n = grid.line(Point(0, 0), Point(1, 2))
    assert(2 === n.size)
  }

  test("lines: equality") {
    val grid = new Grid(3)
    val l = grid.line(Point(0, 0), Point(1, 1))
    val m = grid.line(Point(1, 1), Point(2, 2))

    assert(l === m)
    assert(l.hashCode === m.hashCode)

    val n = grid.line(Point(0, 0), Point(1, 2))
    assertFalse(l == n)
  }

  test("calculating all lines on a grid") {
    var grid = new Grid(0)
    def q(x1: Int, y1: Int, x2: Int, y2: Int) =
      grid.line(Point(x1, y1), Point(x2, y2))
    assert(grid.lines === Set.empty)

    grid = new Grid(1)
    assert(grid.lines === Set(
      q(0, 0, 0, 1), q(1, 0, 1, 1), q(0, 0, 1, 0), q(0, 1, 1, 1), q(0, 0, 1, 1), q(0, 1, 1, 0)
    ))

    grid = new Grid(2)
    assert(grid.lines === Set.empty ++ Set(
      q(0, 0, 0, 2), q(1, 0, 1, 2), q(2, 0, 2, 2), // verticals
      q(0, 0, 2, 0), q(0, 1, 2, 1), q(0, 2, 2, 2), // horizontals
      q(0, 0, 2, 2), q(0, 1, 1, 2), q(1, 0, 2, 1), // diagonals /
      q(0, 1, 1, 0), q(0, 2, 2, 0), q(1, 2, 2, 1), // diagonals \
      q(0, 0, 2, 1), q(0, 1, 2, 2), // shallow /
      q(0, 1, 2, 0), q(0, 2, 2, 1), // shallow \
      q(0, 0, 1, 2), q(1, 0, 2, 2), // shallow /
      q(0, 2, 1, 0), q(1, 2, 2, 0) // shallow \
    ))
  }

  test("counting lines on a grid") {
    def countLines(n: Int) = new Grid(n).countLines

    assert(((0 to 3) map countLines) === List(
      0, 6, 20, 62 // <= take this last number on trust, for now
    ))
  }

  test("counting lines as a map - with sizes") {
    def mapLines(n: Int) = new Grid(n).mapLines

    assert(((0 to 2) map mapLines) === List[Map[Int, Int]](
      Map.empty,
      Map(2 -> 6),
      Map(2 -> 12, 3 -> 8)
    ))
  }

  test("calculating T") {
    assert(Titanic.T(0) === 0)
    assert(Titanic.T(1) === 11)
    assert(Titanic.T(2) === 494)
    assert(Titanic.T(3) === 65465)
    //    assert(Titanic.T(4) === 33554178)
  }

  test("calculating T, method 2") {
    assert(Titanic.T2(0) === 0)
    assert(Titanic.T2(1) === 11)
    assert(Titanic.T2(2) === 494)
    assert(Titanic.T2(3) === 65465)
    assert(Titanic.T2(4) === 33554178)
  }
}
