package com.fdilke.mottoes

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class FormTest extends FunSpec {
  describe("Basic forms") {
    it("can be created and stringified") {
      val basicForm: Form = BasicForm(0)
      basicForm.toString shouldBe "A"
    }

    it("can be initialized from letters") {
      BasicForm(0) shouldBe Form('A')
    }
//    it("have sane semantics of equality") {
//
//    }
  }

  describe("Compound forms") {

    it("can be created and stringified") {
      val basicFormB : Form = BasicForm(1)
      val basicFormC : Form = BasicForm(2)
      val compoundForm: Form = CompoundForm(basicFormB, basicFormC)
      compoundForm.toString shouldBe "(B :> C)"
    }

    // add semantics of equality
  }
}
