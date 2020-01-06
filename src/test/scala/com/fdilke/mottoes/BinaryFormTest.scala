package com.fdilke.mottoes

import org.scalatest.matchers.should.Matchers._
import StandardLetters._
import FormMatchers._
import org.scalatest.funspec.AnyFunSpec

class BinaryFormTest extends AnyFunSpec {
  describe("Basic forms") {
    it("can be created and stringified") {
      val basicForm: BinaryForm = BasicForm(0)
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

  describe("Compound binary forms") {

    it("can be created and stringified") {
      val basicFormB: BinaryForm = BasicForm(1)
      val basicFormC: BinaryForm = BasicForm(2)
      val compoundForm: BinaryForm = CompoundBinaryForm(basicFormB, basicFormC)
      compoundForm.toString shouldBe "(B :> C)"
    }

    it("have a convenience constructor") {
      A :> B shouldBe CompoundBinaryForm(A, B)
      A :> B :> C shouldBe ((A :> B) :> C)
      A :> B :> C should not be (A :> (B :> C))
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

  describe("Binary form helper methods") {
    it("calculates first occurrences") {
      Form.firstOccurrences(Seq(
        17, 0, 0, 17, 2, 17, 1, 0, 5
      )) shouldBe Seq(
        17, 0, 2, 1, 5
      )
    }
  }

  describe("simple binary form properties") {
    it("include 'size'") {
      A should have size 1
      A :> B should have size 2
      A :> (B :> C) should have size 3
      (A :> B) :> C should have size 3
    }

    it("include 'letters'") {
      A.letters shouldBe Seq('A')
      (A :> B).letters shouldBe Seq('A', 'B')
      (A :> (B :> C)).letters shouldBe Seq('A', 'B', 'C')
      ((A :> B) :> (C :> D)).letters shouldBe Seq('A', 'B', 'C', 'D')
    }

    it("include 'is canonical'") {
      A shouldBe 'canonical
      B should not be canonical

      (A :> A) shouldBe canonical
      (A :> B) shouldBe canonical
      (B :> A) should not be canonical
      (A :> C) should not be canonical

      (A :> (A :> A)) shouldBe canonical
      (A :> (A :> B)) shouldBe canonical
      (A :> (B :> A)) shouldBe canonical
      (A :> (A :> C)) should not be canonical
      (A :> (C :> A)) should not be canonical
      ((A :> B) :> (C :> A)) shouldBe canonical
    }
  }

  describe("Conversion from binary to multiary forms") {
    it("works as expected") {
      A.toMultiary shouldBe A
      (A :> B).toMultiary shouldBe B.from(A)
      (A :> (B :> C)).toMultiary shouldBe C.from(A, B)
      ((A :> B) :> C).toMultiary shouldBe C.from(B.from(A))
      ((A :> B) :> (C :> A)).toMultiary shouldBe A.from(B.from(A), C)
    }

    it("does not lose information in a round trip") {
      def checkRoundTrip(form: BinaryForm): Unit = {
        form.toMultiary.toBinary shouldBe form
      }

      checkRoundTrip(A)
      checkRoundTrip(A :> A)
      checkRoundTrip(A :> B)
      checkRoundTrip((A :> B) :> (B :> C))
      checkRoundTrip((A :> B) :> (B :> C) :> D)
      checkRoundTrip((A :> B) :> (A :> C) :> A)
      checkRoundTrip(((A :> B) :> (B :> C)) :> ((C :> B) :> (B :> A)))
    }
  }
}
