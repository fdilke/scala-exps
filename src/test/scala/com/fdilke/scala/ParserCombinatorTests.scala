package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}

import scala.util.parsing.combinator.JavaTokenParsers
import Matchers._

case class Specco(expression: String) {
  def parsed = JsonParser.doParse(expression)
}

object SpeccoHelpers {
  implicit class SpeccoHelper(val sc: StringContext) extends AnyVal {
    def specco(args: Any*): Specco = Specco(sc.parts.mkString)
  }
}

object JsonParser extends JavaTokenParsers {
  def value : Parser[Any] = map | array | stringLiteral |
    floatingPointNumber | "null" | "true" | "false"
  def map : Parser[Any] = "{" ~ repsep(member, ",") ~ "}"
  def array : Parser[Any] = "[" ~ repsep(value, ",") ~ "]"
  def member : Parser[Any] = stringLiteral ~ ":" ~ value

  def doParse(expression: String) =
    parseAll(value, expression)
}

class ParserCombinatorTests extends FunSpec {
  import SpeccoHelpers.SpeccoHelper
  describe("The parse combinators") {
    it("can parse numeric constants") {
      val specco = specco"2"
      specco shouldBe a[Specco]
    }
    it("can parse maps") {
      val specco = specco"""
        { shoeSize: 14 }
      """
      specco shouldBe a[Specco]
//      println("specco = " + specco)
    }
  }
}
