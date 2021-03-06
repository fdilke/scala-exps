package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._
import org.scalatest.freespec.AnyFreeSpec

class CaseObjectTests extends AnyFreeSpec {

  class Widget(
    val property: String
  )

  private val sampleWidget = new Widget("sample")
  case object SampleWidget extends  Widget("sample")
  case object SampleWidget2 extends  Widget("sample")

  "case objects" - {
    "have sane equality semantics" in {
      SampleWidget shouldBe SampleWidget
      SampleWidget should not be sampleWidget
      sampleWidget should not be SampleWidget
      SampleWidget should not be SampleWidget2
    }
  }
}
