package com.fdilke.scala

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

import scala.util.parsing.combinator.RegexParsers

sealed trait LaTeXToken

case class ExpressionToken(expression: String) extends LaTeXToken
case class TextToken(text: String) extends LaTeXToken
case class DefinitionToken(tokens: Seq[LaTeXToken]) extends LaTeXToken
case class AnnotatedToken(heading: String, tokens: Seq[LaTeXToken]) extends LaTeXToken

object PartialLaTeXParser extends RegexParsers {

  def symbols: Parser[String] =
    """[a-zA-Z1-9_]+""".r

  def value : Parser[Seq[LaTeXToken]] =
    rep(token)

  def token: Parser[LaTeXToken] =  // textToken | latexExpression
    definitionToken | annotatedToken // | textToken

  def textToken: Parser[LaTeXToken] =
    symbols ^^ { TextToken }

  def annotatedToken: Parser[LaTeXToken] =
    (("\\\\".r ~> symbols <~ "\\{".r) ~ (value <~ "\\}".r)) ^^ {
      case heading ~ tokens => AnnotatedToken(heading, tokens)
    }

  def definitionToken: Parser[LaTeXToken] =
    (beginDefinition ~> value <~ endDefinition) ^^ { DefinitionToken }

  def beginDefinition: Parser[String] =
    "\\\\begin\\{definition\\}".r

  def endDefinition: Parser[String] =
    "\\\\end\\{definition\\}".r

  def apply(text: String): ParseResult[Seq[LaTeXToken]] =
    parseAll(value, text)
}

class AmbiguousParserCombinatorTests extends FreeSpec {
  "x" - {
    "y" in {
      val parsed = PartialLaTeXParser("\\begin{definition} \\end{definition}")
      parsed.successful shouldBe true
      parsed.get shouldBe Seq(
        DefinitionToken(
          Seq(

          )
        )
      )
    }
  }
}
