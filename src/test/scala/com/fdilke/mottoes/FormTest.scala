package com.fdilke.mottoes

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class FormTest extends FunSpec {
  describe("Forms") {
    it("basic forms can be created and stringified") {
      val basicForm : Form = new BasicForm(0)
      basicForm.toString shouldBe "A"
    }
  }
}
