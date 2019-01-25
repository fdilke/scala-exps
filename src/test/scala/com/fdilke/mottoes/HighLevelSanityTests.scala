package com.fdilke.mottoes

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class HighLevelSanityTests extends FunSpec {
  it("the concatenation operator respects conversions to binary") {
    for {
      form <- EnumerateForms(3)
      form2 <- EnumerateForms(3)
    } {
//      println(s"verifying relation for form = $form, form2 = $form2")
      form :: form2 shouldBe {
        CompoundBinaryForm(
          form.toBinary,
          form2.toBinary
        ).toMultiary
      }
    }
  }
}
