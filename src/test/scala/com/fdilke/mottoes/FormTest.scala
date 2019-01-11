package com.fdilke.mottoes

import org.scalatest.FunSpec
import org.scalatest.Matchers._
import StandardLetters._

class FormTest extends FunSpec {
  describe("Basic forms") {
    it("can be created and stringified") {
      val basicForm: Form = BasicForm(0)
      basicForm.toString shouldBe "A"
    }

    it("can be initialized from letters") {
      BasicForm(0) shouldBe Form('A')
    }

    it("have sane semantics of equality") {
        A shouldBe A
        A shouldBe Form('A')
        A shouldBe BasicForm(0)
        A shouldNot be(B)
        A shouldNot be(Form('B'))
        A shouldNot be(BasicForm(1))
    }
  }

  describe("Compound forms") {

    it("can be created and stringified") {
      val basicFormB : Form = BasicForm(1)
      val basicFormC : Form = BasicForm(2)
      val compoundForm: Form = CompoundForm(basicFormB, basicFormC)
      compoundForm.toString shouldBe "(B :> C)"
    }

    it("have a convenience constructor") {
      A :> B shouldBe CompoundForm(A, B)
    }

    it("have sane semantics of equality") {
      val AA = A :> A
      val AB = A :> B
      val BA = B :> A
      A :> A shouldBe AA
      A :> A should not be AB
      AB should not be BA
      A :> B shouldBe AB
    }
  }
}
