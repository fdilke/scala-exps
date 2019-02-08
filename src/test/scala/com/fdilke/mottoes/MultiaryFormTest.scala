package com.fdilke.mottoes

import com.fdilke.mottoes.StandardLetters._
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import FormMatchers._

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

    it("have a 'size' property") {
      A should have size 1
      (B from A) should have size 2
      (C from (A, B)) should have size 3
      (C from (B from A)) should have size 3
    }

    it("include a 'letters' property") {
      A.letters shouldBe Seq('A')
      (B from A).letters shouldBe Seq('A', 'B')
      (C from (A, B)).letters shouldBe Seq('A', 'B', 'C')
      (D from (B from A, C)).letters shouldBe Seq('A', 'B', 'C', 'D')
    }

    it("can be converted to binary") {
        A.toMultiary shouldBe A
        B.from(A).toBinary shouldBe (A :> B)
        C.from(A, B).toBinary shouldBe (A :> (B :> C))
        C.from(B.from(A)).toBinary shouldBe ((A :> B) :> C)
        A.from(B.from(A), C).toBinary shouldBe ((A :> B) :> (C :> A))
      }

    it("admit a concatenation operator") {
      A :: B shouldBe (B from A)
      A :: (C from B) shouldBe (C from(A, B))
      (B from A) :: (D from C) shouldBe (D from(B from A, C))
      C.from(A, B) :: F.from(D, E) shouldBe F.from(
        C.from(A, B),
        D,
        E
      )
    }

    it("- - -") {
      (A from A) :: A shouldBe (A from (A from A))
    }

    it("can be converted to binary and back without losing information") {
      def checkRoundTrip(mform: MultiaryForm): Unit = {
        mform.toBinary.toMultiary shouldBe mform
      }

      checkRoundTrip(A)
      checkRoundTrip(A from A)
      checkRoundTrip(B from A)
      checkRoundTrip(C.from(B, B from A))
      checkRoundTrip(D.from(B from A, C from B))
      checkRoundTrip(A from (B from A, C from A))
      checkRoundTrip(A from ( C from (B from A, B), B from C, B))
    }

    it("can be checked for unique solutions") {
      A should not be uniquelySolvable

      (A from A) shouldBe uniquelySolvable
      (B from B) shouldBe uniquelySolvable
      (B from A) should not be uniquelySolvable

      C.from(A, B) should not be uniquelySolvable
      A.from(B, B) should not be uniquelySolvable
      B.from(B, B) should not be uniquelySolvable

      B.from(B from A, A) shouldBe uniquelySolvable
      C.from(B from A, B) should not be uniquelySolvable
      A.from(B from A, B) should not be uniquelySolvable
      A.from(A from A, A) should not be uniquelySolvable

      A.from(A, A, A) should not be uniquelySolvable
    }

    it("can test unique solvability in an awkward case") {
      val awkward = A from(A from(A from A))
      println("testing awkward case: " + awkward)
      awkward shouldBe uniquelySolvable
    }

    it("can be checked for unique solutions - even when we don't want to admit these cases") {
      A.from(A, B) shouldBe uniquelySolvable
      B.from(A, B) shouldBe uniquelySolvable
      B.from(A, A, B) shouldBe uniquelySolvable
    }
  }
}
