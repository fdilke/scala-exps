package com.fdilke.scala

import org.scalatest.{FunSpec, Matchers}

import scala.util.parsing.combinator.JavaTokenParsers
import Matchers._
import scala.language.implicitConversions
import scala.language.reflectiveCalls
import scala.util.parsing.input.Positional

class ParserCombinatorTests extends FunSpec {

  describe("The Specco parser") {
    case class Specco(expression: String) {
      def parsed = SpeccoParser.doParse(expression)
    }

    object SpeccoHelpers {
      implicit def speccoHelper(sc: StringContext) = new Object {
        def specco(args: Any*): Specco = Specco(sc.parts.mkString)
      }
    }

    object SpeccoParser extends JavaTokenParsers {
      def value : Parser[Any] = map | array | stringLiteral |
        floatingPointNumber | "null" | "true" | "false"
      def map : Parser[Any] = "{" ~ repsep(member, ",") ~ "}"
      def array : Parser[Any] = "[" ~ repsep(value, ",") ~ "]"
      def member : Parser[Any] = stringLiteral ~ ":" ~ value

      def doParse(expression: String) =
        parseAll(value, expression)
    }

    import SpeccoHelpers.speccoHelper

    it("can parse numeric constants") {
      val specco = specco"2"
      specco shouldBe a[Specco]
    }
    it("can parse maps") {
      val specco = specco"""
        { shoeSize: 14 }
      """
      specco shouldBe a[Specco]
    }
  }

  describe("The Tiddly parser") {

    sealed trait LaTeXToken extends Positional
    case object LeftCurlyBracketToken extends LaTeXToken
    case object RightCurlyBracketToken extends LaTeXToken
    case class AnnotatedToken(
                               heading: String,
                               tokens: Seq[LaTeXToken]
                             ) extends LaTeXToken
    case class SymbolsToken(
                             symbols: String
                           ) extends LaTeXToken

    object TiddlyParser extends JavaTokenParsers {
      def value : Parser[Seq[LaTeXToken]] =
        rep(token)

      def token: Parser[LaTeXToken] =
        positioned {
          annotatedToken | symbolsToken | brackets
            failure("Bad token")
        }

      def symbols: Parser[String] =
        """[a-zA-Z1-9_]+""".r

      def symbolsToken: Parser[LaTeXToken] =
        """[a-zA-Z1-9_]+""".r ^^ { SymbolsToken(_) }

      def annotatedToken: Parser[LaTeXToken] =
        (("\\\\".r ~> symbols <~ "\\{".r) ~ (value <~ "\\}".r)) ^^ {
          case heading ~ tokens => AnnotatedToken(heading, tokens)
        }

      def brackets: Parser[LaTeXToken] =
        leftCurlyBracket | rightCurlyBracket

      def leftCurlyBracket: Parser[LaTeXToken] =
        "\\{".r ^^^ LeftCurlyBracketToken

      def rightCurlyBracket: Parser[LaTeXToken] =
        "\\}".r ^^^ RightCurlyBracketToken

      def doParse(expression: String): Seq[LaTeXToken] =
        parseAll(value, expression).get
    }

    ignore("can parse numeric constants") {
      TiddlyParser.doParse("2") shouldBe Seq(
        SymbolsToken("2")
      )

    }
    ignore("can parse annotations") {
      TiddlyParser.doParse("""
        \rubbish{garbage}
      """) shouldBe Seq()
    }
  }
}
