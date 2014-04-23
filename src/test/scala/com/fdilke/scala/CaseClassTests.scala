package com.fdilke.scala

import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory
import org.junit.Assert
import Assert._

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
    assert(binOp.isInstanceOf[Expression])
  }

  test("using fields") {
    val x = Var("x")
    assert(x.name === "x")

    val binOp = BinaryOp("+", Number(1.0), Var("x"))
    assert(binOp.operator === "+")
  }

  test("using toString") {
    val x = Var("x")
    assert(x.toString === "Var(x)")

    val binOp = BinaryOp("+", Number(1.0), Var("x"))
    assert(binOp.toString === "BinaryOp(+,Number(1.0),Var(x))")
  }

  test("equality") {
    val x = Var("x")
    val x2 = Var("x")
    val y = Var("y")
    assert(x === x2)
    assertFalse(x == y)

    assert(BinaryOp("+", x, Number(1.0)) === BinaryOp("+", x2, Number(1.0)))
    assertFalse(BinaryOp("-", x, Number(1.0)) == BinaryOp("+", y, Number(1.0)))
  }

  test("simplifying expressions with operators") {
    val x = Var("x")

    // --x = x
    assert(x === -(-x))

    // x * 0 = 0
    assert(Number(0) === x * 0)

    // 0 * x = 0
    assert(Number(0) === 0 * x)

    // x + 0 = x
    assert(x === x + 0)

    // 0 + x = x
    assert(x === 0 + x)

    // x * 1 = x
    assert(x === x * 1)

    // 1 * x = x
    assert(x === 1 * x)
  }

  test("compound simplifications") {
    val x = Var("x")

    assert(x == -(-(x + 0)))
    assert(UnaryOp("abs", x) == UnaryOp("abs", UnaryOp("-", UnaryOp("-", x + 0))).simplify)

    assert(x === 1 * (0 + x))
    assert(x === 1 * (x + 0))
    assert(x === 0 + (x * 1))
    assert(1 + x === 0 + ((1 + x) * 1))
    assert(1 + x === 1 + BinaryOp("+", 0, x * 1))
  }

  test("description") {
    assert(Number(3).describe === "a number")
    assert(Var("x").describe === "a variable")
    assert("a variable + a number" === (Var("x") + 1).describe)
    assert("abs(a number)" === UnaryOp("abs", 3).describe)
  }

  test("use in defining vals/vars") {
    val x = Var("x")
    val Var(xName) = x
    assert(xName === "x")
  }

  test("use of option") {
    val capitals = Map("France" -> "Paris", "Japan" -> "Tokyo", "UK" -> "London")
    val beijing : Option[String]= capitals.get("China")
    assert("N/A" === (beijing match {
      case Some(x : String) => x
      case None => "N/A"
    }))
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

    assert(output === List("3.0", "x", "[a variable + a number * a number]", "-[a number * a variable]"))
  }

  test("case sequences as function literals II") {
    val x = Var("x")

    assert(1 == (List[Expression](3, x, (x+1)*5, -(7*x), (x+1)*(2*x)) count {
      case BinaryOp(_, left : BinaryOp, right : BinaryOp) => true
      case _ => false
    }))
  }
}
