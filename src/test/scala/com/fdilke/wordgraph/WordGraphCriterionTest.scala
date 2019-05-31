package com.fdilke.wordgraph

import org.scalatest.{FunSpec, Matchers}
import Matchers._
import com.fdilke.wordgraph.WordGraphCriterion.adjacent

class WordGraphCriterionTest extends FunSpec {
  describe("The adjacency criterion") {
    it("has the right basic properties") {
      adjacent("foo", "foo") shouldBe false
      adjacent("foo", "bar") shouldBe false
      adjacent("bar", "baz") shouldBe true
    }

    it("covers awkward cases") {
      adjacent("foo", "FOO") shouldBe false
      adjacent("Ok", "North") shouldBe true
      adjacent("U Bend", "A Z") shouldBe false
    }
  }
}
