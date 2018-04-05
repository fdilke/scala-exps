package com.fdilke.scala

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

import scala.util.parsing.combinator.RegexParsers

trait TestToken
case class DummyTestToken(text: String) extends TestToken

object TestParser extends RegexParsers {

  def plainEquation: Parser[TestToken] =
    ("\\\\\\[".r ~> "[^(\\\\\\])]+".r <~ "\\\\\\]".r) ^^ {
      DummyTestToken
    }

  def apply(text: String): ParseResult[TestToken] =
    parseAll(plainEquation, text)
}

class MoreParserCombinatorTests extends FreeSpec {
  "x" - {
    "y" in {
      val parsed = TestParser("\\[ x \\]")
      parsed shouldBe 'successful
      parsed.get shouldBe DummyTestToken("x ")
    }
  }
}
