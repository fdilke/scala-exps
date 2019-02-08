package com.fdilke.mottoes

import com.fdilke.mottoes.StandardLetters._
import org.scalatest.FunSpec
import org.scalatest.Matchers._
//import FormMatchers._

class FormSolverTest extends FunSpec {
  describe("The form solver") {
    it("can detect when a form is uniquely solvable") {
      val solution = FormSolver(B from(A, B))
      solution shouldBe Some(Set(B))
    }
  }
}
