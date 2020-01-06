package com.fdilke.mottoes

import org.scalatest.matchers.should.Matchers._
import org.scalatest.funspec.AnyFunSpec

class HighLevelSanityTests extends AnyFunSpec {
  it("the concatenation operator respects conversions to binary") {
    for {
      form <- EnumerateForms(3)
      form2 <- EnumerateForms(3)
    } {
      form :: form2 shouldBe {
        CompoundBinaryForm(
          form.toBinary,
          form2.toBinary
        ).toMultiary
      }
    }
  }

  it("two concepts of unique solvability coincide") {
    for {
      form <- EnumerateForms(4)
    } {
      val usolve1 = form.isUniquelySolvable
      val usolve2 = FormSolver(form).isDefined

      usolve1 shouldBe usolve2
    }
  }

  // TODO: FIX
  ignore("when we can solve a form uniquely, we always use more than 0 arguments, which are from the form itself") {
    for {
      form <- EnumerateForms(4)
    } {
      println(s"Enumerating: $form")
      FormSolver(form) match {
        case None =>
        case Some(argsUsed) =>
          argsUsed should not be empty
          form match {
            case _: BasicForm => fail("can't solve basic form")
            case CompoundMultiaryForm(args, _) =>
              args.toSet should contain allElementsOf(argsUsed)
          }
      }
    }
  }
}
