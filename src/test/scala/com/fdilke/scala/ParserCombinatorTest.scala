package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._
import org.scalatest.freespec.AnyFreeSpec

import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.input.{NoPosition, Position, Positional}

class ParserCombinatorTest extends AnyFreeSpec {

  trait Token extends Positional
  case class SymbolToken(text: String) extends Token

  class SimpleParserException(
    val message: String,
    val pos: Position = NoPosition
  ) extends Exception(message)

  "a basic parser" in {
    object SimpleParser extends RegexParsers {
      def symbolToken: Parser[Token] =
        """[a-z]+""".r ^^ SymbolToken

      def value: Parser[Seq[Token]] =
        rep(symbolToken)

      def apply(text: String): Seq[Token] =
        parseAll(value, text).get
    }
    SimpleParser("one two three") shouldBe Seq(
      SymbolToken("one"),
      SymbolToken("two"),
      SymbolToken("three")
    )
    intercept[RuntimeException] {
      SimpleParser("AAGH")
    }
  }

  "a basic parser with a fail mechanism" in {
    object SimpleParser extends RegexParsers {
      def symbolToken: Parser[Token] =
        """[a-z]+""".r ^^ SymbolToken

      def token: Parser[Token] =
        symbolToken | failure("invalid")

      def value: Parser[Seq[Token]] =
        rep(token)

      def apply(text: String): Seq[Token] = {
        parseAll(value, text) match {
          case Success(parsed, _) => parsed
          case NoSuccess(msg, _) => throw new SimpleParserException(msg)
        }
      }
    }
    SimpleParser("one two three") shouldBe Seq(
      SymbolToken("one"),
      SymbolToken("two"),
      SymbolToken("three")
    )
    intercept[SimpleParserException] {
      SimpleParser("AAGH")
    }
  }

  "a basic parser with a fail mechanism and positionality" in {
    object SimpleParser extends RegexParsers {
      def symbolToken: Parser[Token] =
        """[a-z]+""".r ^^ SymbolToken

      def token: Parser[Token] =
        positioned {
          symbolToken | failure("invalid")
        }

      def value: Parser[Seq[Token]] =
        rep(token)

      def apply(text: String): Seq[Token] = {
        parseAll(value, text) match {
          case Success(parsed, _) => parsed
          case NoSuccess(msg, input) =>
            throw new SimpleParserException(msg, input.pos)
        }
      }
    }
    val parseResult = SimpleParser("one two three")
    parseResult map {
      token => (token, token.pos.line, token.pos.column)
    } shouldBe Seq(
      (SymbolToken("one"), 1, 1),
      (SymbolToken("two"), 1, 5),
      (SymbolToken("three"), 1, 9)
    )
    parseResult.map{ _.pos }
    intercept[SimpleParserException] {
      SimpleParser("AAGH")
    }.pos.toString shouldBe "1.1"
  }
}
