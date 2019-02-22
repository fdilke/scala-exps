package com.fdilke.mottoes

import com.fdilke.mottoes.StandardLetters._
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import FormMatchers._

class EnumerateFormsTest extends FunSpec {

    describe("enumeration of forms") {
      it("covers the case of length 0") {
        EnumerateForms(0) shouldBe Seq()
        EnumerateForms(0, 1) shouldBe Seq()
        EnumerateForms(0, 2) shouldBe Seq()
      }

      it("covers the case of length 1") {
        EnumerateForms(1) shouldBe Seq(A)
        EnumerateForms(1, 1) shouldBe Seq(A, B)
        EnumerateForms(1, 2) shouldBe Seq(A, B, C)
      }

      it("covers the case of length 2") {
        EnumerateForms(2) shouldBe Seq(
          A from A,
          B from A
        )
        EnumerateForms(2, 1) shouldBe Seq(
          A from A,
          B from A,
          A from B,
          B from B,
          C from B
        )
        EnumerateForms(2, 2) shouldBe Seq(
          A from A,
          B from A,
          A from B,
          B from B,
          C from B,
          A from C,
          B from C,
          C from C,
          D from C
        )
      }
      it("covers the case of length 3") {
        EnumerateForms(3) shouldBe Seq(
          A from (A, A),
          B from (A, A),
          A from (A, B),
          B from (A, B),
          C from (A, B),
          A from (A from A),
          B from (A from A),
          A from (B from A),
          B from (B from A),
          C from (B from A)
        )
      }
      it("enumerates only canonical forms") {
        for {
          form <- EnumerateForms(4)
        }
          form shouldBe canonical
      }
    }
}
