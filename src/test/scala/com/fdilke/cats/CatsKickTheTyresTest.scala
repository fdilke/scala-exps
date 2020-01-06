package com.fdilke.cats

// all a bit broken because as of Jan 2020, Cats doesn't support Scala 2.13.1, only 2.13.0
//import cats.Eq
//import cats.instances.int._
//import cats.instances.string._
//import cats.syntax.eq._
import org.scalatest.funspec.AnyFunSpec

import scala.language.implicitConversions
import org.scalatest.matchers.should.Matchers._
//import export.imports

class CatsKickTheTyresTest extends AnyFunSpec {
  describe("Equality.... note, broken") {
    it("can be detected type-safely") {
//      val eqInt = Eq[Int]
//      eqInt.eqv(5, 7) shouldBe false
//// TODO: make these work! Then apply to dotty_exps which has the same issue
////      (5 === 7) shouldBe false
////      ("mog" === "m" + "og") shouldBe false
//      Eq[String].eqv("mog", "m" + "og") shouldBe true
    }
  }
}

