package com.fdilke.scala

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

import scala.util.parsing.combinator.RegexParsers

sealed trait LaTeXToken

case class ExpressionToken(expression: String) extends LaTeXToken
case class TextToken(text: String) extends LaTeXToken {
  println(s"created TextToken($text)")
}
case class DefinitionToken(tokens: Seq[LaTeXToken]) extends LaTeXToken
case class AnnotatedToken(heading: String, tokens: Seq[LaTeXToken]) extends LaTeXToken

object PartialLaTeXParser extends RegexParsers {

  def symbols: Parser[String] =
    """[a-zA-Z1-9_]+""".r

  def value : Parser[Seq[LaTeXToken]] =
    rep(token) ^^ { valueTokens => println("value tokens: [" + valueTokens + "]") ; valueTokens }

  def token: Parser[LaTeXToken] =
    definitionToken | annotatedToken // | textToken - can restore this??

  def textToken: Parser[LaTeXToken] =
    symbols ^^ { TextToken }

  def annotatedToken: Parser[LaTeXToken] =
    (("\\\\".r ~> symbols <~ "\\{".r) ~ (value <~ "\\}".r)) ^^ {
      makeAnnotatedToken
    }

  private def makeAnnotatedToken(
    headingTokens: String ~ Seq[LaTeXToken]
  ) = {
    val heading ~ tokens = headingTokens
    AnnotatedToken(heading, tokens)
  }

  def definitionToken: Parser[LaTeXToken] =
    (beginDefinition ~> value <~ endDefinition) ^^ { DefinitionToken }

  def beginDefinition: Parser[String] =
    "\\\\begin\\{definition\\}".r

  def endDefinition: Parser[String] =
    "\\\\end\\{definition\\}".r

  def asValue(text: String): ParseResult[Seq[LaTeXToken]] =
    parseAll(value, text)

  def asDefinition(text: String): ParseResult[LaTeXToken] =
    parseAll(definitionToken, text)
}

class AmbiguousParserCombinatorTests extends FreeSpec {
  "An ambiguous grammar using |" - {
    "uses the left hand term" in {
      val parsed = PartialLaTeXParser.asDefinition("\\begin{definition} \\end{definition}")
      parsed.successful shouldBe true
      parsed.get shouldBe
        DefinitionToken(
          Seq(
          )
      )
    }
    "can parse space as value" in {
      val parsed = PartialLaTeXParser.asValue(" ")
      parsed.successful shouldBe true
      parsed.get shouldBe Seq()
    }
  }
}
