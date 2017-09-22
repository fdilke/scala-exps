package com.fdilke.scala

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class OverridingMembersTest extends FlatSpec {

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
