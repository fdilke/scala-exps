package com.fdilke.scala

import org.scalatest.{ShouldMatchers, FunSuite}
import org.scalamock.scalatest.MockFactory
import org.junit.Assert
import Assert._
import org.scalatest.matchers.ShouldMatchers
import ShouldMatchers._
import FelixMatchers._

object Expression {
  implicit def numberAsExpression(x : Double) = Number(x)
  implicit def numberAsExpression(x : Int) = Number(x)
}

sealed abstract class Expression {
  def + (that:Expression) : Expression = BinaryOp("+", this, that).simplify
  def * (that:Expression) : Expression = BinaryOp("*", this, that).simplify
  def unary_- () : Expression = UnaryOp("-", this).simplify

  def simplify : Expression = this match {
    case UnaryOp("-", UnaryOp("-", e)) => e.simplify
    case BinaryOp("*", e, Number(0)) => Number(0)
    case BinaryOp("*", Number(0), e) => Number(0)
    case BinaryOp("+", e, Number(0)) => e.simplify
    case BinaryOp("+", Number(0), e) => e.simplify
    case BinaryOp("*", e, Number(1)) => e.simplify
    case BinaryOp("*", Number(1), e) => e.simplify
    // if we can't simplify, push it one level down
    case BinaryOp(op, left, right) => BinaryOp(op, left simplify, right simplify)
    case UnaryOp(op, x) => UnaryOp(op, x simplify)
    case _ => this
  }

  def describe() : String = this match {
    case Number(_) => "a number"
    case Var(_) => "a variable"
    case BinaryOp(op,left,right) => left.describe + " " + op + " " + right.describe
    case UnaryOp(op,e) => op + "(" + e.describe + ")"
  }
}

case class Var(name: String) extends Expression
case class Number(num:Double) extends Expression
case class UnaryOp(operator: String, arg:Expression) extends Expression
case class BinaryOp(operator: String, left:Expression, right:Expression) extends Expression

class CaseClassTests extends FunSuite with MockFactory {
  test("using factory methods") {
    val x = Var("x")
    assert(x.isInstanceOf[Var])

    val binOp = BinaryOp("+", Number(1.0), Var("x"))
    binOp should be ( anInstanceOf[Expression] )
  }

  test("using fields") {
    Var("x") should have ('name ("x"))

    val binOp = BinaryOp("+", Number(1.0), Var("x"))
    binOp.operator should be ("+")
  }

  test("using toString") {
    val x = Var("x")
    assert(x.toString === "Var(x)")

    val binOp = BinaryOp("+", Number(1.0), Var("x"))
    binOp.toString should be ("BinaryOp(+,Number(1.0),Var(x))")
  }

  test("equality") {
    val x = Var("x")
    val x2 = Var("x")
    val y = Var("y")
    assert(x === x2)
    assertFalse(x == y)

    BinaryOp("+", x, Number(1.0)) shouldBe BinaryOp("+", x2, Number(1.0))
    BinaryOp("-", x, Number(1.0)) should not be BinaryOp("+", y, Number(1.0))
  }

  test("simplifying expressions with operators") {
    val x = Var("x")

    -(-x) shouldBe x

    (x * 0) shouldBe Number(0)
    (0 * x) shouldBe Number(0)

    (x + 0) shouldBe x
    (0 + x) shouldBe x

    (x * 1) shouldBe x
    (1 * x) shouldBe x
  }

  test("compound simplifications") {
    val x = Var("x")

    -(-(x + 0)) shouldBe x
    UnaryOp("abs", UnaryOp("-", UnaryOp("-", x + 0))).simplify shouldBe UnaryOp("abs", x)

    1 * (0 + x) shouldBe x
    1 * (x + 0) shouldBe x
    0 + (x * 1) shouldBe x
    0 + ((1 + x) * 1) shouldBe 1 + x
    1 + BinaryOp("+", 0, x * 1) shouldBe 1 + x
  }

  test("description") {
    Number(3).describe shouldBe "a number"
    Var("x").describe shouldBe "a variable"
    (Var("x") + 1).describe shouldBe "a variable + a number"
    UnaryOp("abs", 3).describe shouldBe "abs(a number)"
  }

  test("use in defining vals/vars") {
    val x = Var("x")
    val Var(xName) = x
    xName shouldBe "x"
  }

  test("use of option") {
    val capitals = Map("France" -> "Paris", "Japan" -> "Tokyo", "UK" -> "London")
    val beijing : Option[String]= capitals.get("China")
    (beijing match {
      case Some(x : String) => x
      case None => "N/A"
    }) shouldBe "N/A"
  }

  test("case sequences as function literals") {
    val x = Var("x")
    val exprs = List[Expression](3, x, (x+1)*5, -(7*x))

    val output : List[String] = exprs map {
      case Var(name) => name
      case BinaryOp(op, left, right) => "[" + left.describe + " " + op + " " + right.describe + "]"
      case UnaryOp(op, e) => op + "[" + e.describe + "]"
      case Number(num) => num toString()
    }

    output shouldBe List("3.0", "x", "[a variable + a number * a number]", "-[a number * a variable]")
  }

  test("case sequences as function literals II") {
    val x = Var("x")

    (List[Expression](3, x, (x+1)*5, -(7*x), (x+1)*(2*x)) count {
      case BinaryOp(_, left : BinaryOp, right : BinaryOp) => true
      case _ => false
    }) shouldBe 1
  }
}
