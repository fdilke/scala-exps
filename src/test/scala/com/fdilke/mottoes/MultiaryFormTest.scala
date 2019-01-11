package com.fdilke.mottoes

import com.fdilke.mottoes.FormMatchers._
import com.fdilke.mottoes.StandardLetters._
import org.scalatest.FunSpec
import org.scalatest.Matchers._

class MultiaryFormTest extends FunSpec {
  describe("Compound multiary forms") {

    it("can be created and stringified") {
      val compoundForm: MultiaryForm = CompoundMultiaryForm(Seq(B), C)
      compoundForm.toString shouldBe "(B) >> C"
    }

    it("must have a nonempty argument list") {
      intercept[IllegalArgumentException] {
        CompoundMultiaryForm(Seq(), A)
      }
    }

    it("have a convenience constructor") {
      B.from(A) shouldBe CompoundMultiaryForm(Seq(A), B)
      B.from(A, C) shouldBe CompoundMultiaryForm(Seq(A, C), B)
    }

    it("have sane semantics of equality") {
      val AA = A.from(A)
      val AB = B.from(A)
      val BA = A.from(B)
      A.from(A) shouldBe AA
      A.from(A) should not be AB
      B.from(A) should not be BA
      B.from(A) shouldBe AB
      B.from(A, B) should not be AB
    }
  }
}
