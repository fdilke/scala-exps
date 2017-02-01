package com.fdilke.scala

import org.scalatest.FunSpec

import scala.meta._
import org.scalatest.Matchers._

class MetaProgrammingTests extends FunSpec {
  describe("Metaprogramming") {
    it("can tokenize simple expressions") {
      val tokenized: Tokens = "val x = 2".tokenize.get

      tokenized.syntax shouldBe "val x = 2"
      tokenized should have size 9

      tokenized.structure shouldBe """
          |Tokens(BOF [0..0), val [0..3),
          |[3..4), x [4..5),
          |[5..6), = [6..7),
          |[7..8), 2 [8..9), EOF [9..9))
        """.stripMargin.trim.replace("\n", "   ")

      val boringTokenTypes: Seq[Class[_ <: Token]] =
        Seq(
          classOf[Token.BOF],
          classOf[Token.EOF],
          classOf[Token.Space]
        )
      val tokens = tokenized filter {
        t => !(boringTokenTypes contains t.getClass)
      }
      tokens should have size 4

      tokens map {
        _.structure
      } shouldBe Seq(
        "val [0..3)",
        "x [4..5)",
        "= [6..7)",
        "2 [8..9)"
      )
    }
  }
}

