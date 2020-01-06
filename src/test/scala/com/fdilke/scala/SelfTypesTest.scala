package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class SelfTypesTest extends AnyFunSpec {
  describe("Mutually dependent traits") {
    it("can be used to split a trait into smaller traits") {
      trait Yin { yang: Yang => }
      trait Yang { yin: Yin => }
      new Yin with Yang
    }

    it("can be used to split a trait into smaller traits, with interdependencies") {
      trait Yin { yang: Yang =>
        def ping = pong
        def stop = 1
      }
      trait Yang { yin: Yin =>
        def pong = stop
      }
      (new Yin with Yang).ping shouldBe 1
    }
  }
}
