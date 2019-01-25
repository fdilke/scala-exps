package com.fdilke.mottoes

import com.fdilke.mottoes.StandardLetters._
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import FormMatchers._

class EnumerateFormsTest extends FunSpec {

    describe("enumeration of forms") {
      it("covers the case of length 0") {
        EnumerateForms(0, 0) shouldBe Seq()
        EnumerateForms(0, 1) shouldBe Seq()
        EnumerateForms(0, 2) shouldBe Seq()
      }

      it("covers the case of length 1") {
        EnumerateForms(1, 0) shouldBe Seq(A)
        EnumerateForms(1, 1) shouldBe Seq(A, B)
        EnumerateForms(1, 2) shouldBe Seq(A, B, C)
      }

      it("covers the case of length 2") {
        EnumerateForms(2, 0) shouldBe Seq(
          A from A,
          B from A
        )
      }
    }
}
