package com.fdilke.scala

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers._

class ExtendingInnerTraits extends AnyFreeSpec {
  
  trait HasInner {
    trait Inner {
      def innerMethod = 2
    }

    def inner: Inner =
      new Object with Inner
  }
  
  "Inner traits" - {
    "still work if you don't extend them" in {
      class VanillaHasInner extends HasInner
      val vanilla = new VanillaHasInner
      vanilla.inner.innerMethod shouldBe 2
    }

    "can be extended" in {
      class CustomHasInner extends HasInner {
        trait Inner extends super.Inner {
          def extraMethod = "x"
        }
        override def inner =
          new Object with Inner
      }
      val custom = new CustomHasInner
      custom.inner.innerMethod shouldBe 2
      custom.inner.extraMethod shouldBe "x"
    }

    "can be extended and modified" in {
      class CustomHasInner extends HasInner {
        trait Inner extends super.Inner {
          override def innerMethod = 3
          def extraMethod = "x"
        }
        override def inner =
          new Object with Inner
      }
      val custom = new CustomHasInner
      custom.inner.innerMethod shouldBe 3
      custom.inner.extraMethod shouldBe "x"
    }
  }

}
