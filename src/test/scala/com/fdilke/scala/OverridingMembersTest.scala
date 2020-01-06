package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._
import org.scalatest.flatspec.AnyFlatSpec

class OverridingMembersTest extends AnyFlatSpec {

  trait Doodad[+T] {
    def doThing(arg: Int): T
  }

  class Widget {
    val doodadImpl: Doodad[Any] =
      arg => ???
  }

  object BetterWidget extends Widget {
    override val doodadImpl: Doodad[String] =
      arg => "Hello"
  }

  it should "override a member" in {
    BetterWidget.doodadImpl.doThing(0) shouldBe "Hello"
  }
}
