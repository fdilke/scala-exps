package com.fdilke.cats

import cats.Eq
import cats.instances.int._
import cats.instances.string._
import cats.syntax.eq._
import org.scalatest.FunSpec

import scala.language.implicitConversions
import org.scalatest.Matchers._
import cats.Eq
//import export.imports

class CatsKickTheTyresTest extends FunSpec {
  describe("Equality") {
    it("can be detected type-safely") {
      val eqInt = Eq[Int]
      eqInt.eqv(5, 7) shouldBe false
// TODO: make these work! Then apply to dotty_exps which has the same issue
//      (5 === 7) shouldBe false
//      ("mog" === "m" + "og") shouldBe false
      Eq[String].eqv("mog", "m" + "og") shouldBe true
    }
  }
}

