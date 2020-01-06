package com.fdilke.mottoes

import com.fdilke.mottoes.StandardLetters._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._
//import FormMatchers._

class FormSolverTest extends AnyFunSpec {
  describe("The form solver") {
    it("rejects basic forms and non-uniquely-solvable ones") {
      FormSolver(A) shouldBe None
      FormSolver(A from B) shouldBe None
      FormSolver(C from(A, B)) shouldBe None
      FormSolver(B from(B from A)) shouldBe None
      FormSolver(A from(B, B from A)) shouldBe None
      FormSolver(A from(A, A)) shouldBe None
    }

    it("detects which arguments are used in a uniquely solvable form") {
      FormSolver(A from A) shouldBe Some(Set(A))
      FormSolver(B from(A, B)) shouldBe Some(Set(B))

      // TODO: fix!
      // FormSolver(B from(A, B from A)) shouldBe Some(Set(A, B from A))

      // TODO; fix!
      //      FormSolver(C from(A, B from A, C from B)) shouldBe Some(Set(C from B, A))
    }

    ignore("...") {
      FormSolver(
        A from (A from (A from A))
      ) shouldBe Some(Set(
        A from (A from A)
      ))
    }
  }
}
