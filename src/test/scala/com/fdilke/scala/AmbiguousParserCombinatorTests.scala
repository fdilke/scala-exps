package com.fdilke.scala

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

import scala.util.parsing.combinator.RegexParsers

sealed trait NylonDoodad

case class ExpressionDoodad(expression: String) extends NylonDoodad
case class TextDoodad(text: String) extends NylonDoodad {
  println(s"created TextDoodad($text)")
}
case class DefinitionDoodad(tokens: Seq[NylonDoodad]) extends NylonDoodad
case class AnnotatedDoodad(heading: String, tokens: Seq[NylonDoodad]) extends NylonDoodad

object PartialLaTeXParser extends RegexParsers {

  def symbols: Parser[String] =
    """[a-zA-Z1-9_]+""".r

  def value : Parser[Seq[NylonDoodad]] =
    rep(token) ^^ { valueDoodads => println("value tokens: [" + valueDoodads + "]") ; valueDoodads }

  def token: Parser[NylonDoodad] =
    definitionDoodad | annotatedDoodad // | textDoodad - can restore this??

  def textDoodad: Parser[NylonDoodad] =
    symbols ^^ { TextDoodad }

  def annotatedDoodad: Parser[NylonDoodad] =
    (("\\\\".r ~> symbols <~ "\\{".r) ~ (value <~ "\\}".r)) ^^ {
      makeAnnotatedDoodad
    }

  private def makeAnnotatedDoodad(
    headingDoodads: String ~ Seq[NylonDoodad]
  ) = {
    val heading ~ tokens = headingDoodads
    AnnotatedDoodad(heading, tokens)
  }

  def definitionDoodad: Parser[NylonDoodad] =
    (beginDefinition ~> value <~ endDefinition) ^^ { DefinitionDoodad }

  def beginDefinition: Parser[String] =
    "\\\\begin\\{definition\\}".r

  def endDefinition: Parser[String] =
    "\\\\end\\{definition\\}".r

  def asValue(text: String): ParseResult[Seq[NylonDoodad]] =
    parseAll(value, text)

  def asDefinition(text: String): ParseResult[NylonDoodad] =
    parseAll(definitionDoodad, text)
}

class AmbiguousParserCombinatorTests extends FreeSpec {
  "An ambiguous grammar using |" - {
    "uses the left hand term" in {
      val parsed = PartialLaTeXParser.asDefinition("\\begin{definition} \\end{definition}")
      parsed.successful shouldBe true
      parsed.get shouldBe
        DefinitionDoodad(
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
