package com.fdilke.mottoes

import com.fdilke.mottoes.StandardLetters._
import org.scalatest.FunSpec
import org.scalatest.Matchers._
//import FormMatchers._

class FormSolverTest extends FunSpec {
  describe("The form solver") {
    it("rejects basic forms") {
      FormSolver(A) shouldBe None
    }

    it("can detect when a form is uniquely solvable") {
      FormSolver(B from(A, B)) shouldBe Some(Set(B))
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
