package com.fdilke.wordgraph

import org.scalatest.{FunSpec, Matchers}
import Matchers._

class PositionTest extends FunSpec {
  describe("Allowable moves in a WordGraph position") {
    it("are all, if there is no previous word") {
      Position(
        Set("foo", "bar"),
        None
      ).allowedMoves shouldBe Set(
        "foo", "bar"
      )
    }

    it("are all adjacent to the previous word, if present") {
      Position(
        Set("foo", "bar", "baz"),
        Some("corge")
      ).allowedMoves shouldBe Set(
        "foo", "bar"
      )
    }
  }

  describe("Making a move in a WordGraph position") {
    it("is only allowed if it's in the set") {
      intercept[IllegalArgumentException] {
        Position(Set("foo", "bar"), Some("fred")).move("corge")
      }
    }

    it("is only allowed if it's adjacent") {
      intercept[IllegalArgumentException] {
        Position(Set("foo", "bar", "corge"), Some("baz")).move("corge")
      }
    }

    it("removes the move from the set and stores it as previous") {
      Position(
        Set("foo", "bar", "corge"),
        Some("fred")
      ).move("corge") shouldBe Position(
        Set("foo", "bar"),
        Some("corge")
      )

      Position(
        Set("foo", "bar", "corge"),
        None
      ).move("corge") shouldBe Position(
        Set("foo", "bar"),
        Some("corge")
      )
    }
  }

  describe("Verifying WordGraph positions for P") {
    it("has the right basic properties") {
      Position(Set.empty, None) shouldBe 'P
      Position(Set("foo"), None) should not be 'P
      Position(Set("foo", "bar"), None) should not be 'P
      Position(Set("bar", "baz"), None) shouldBe 'P
    }

    it("depends only on parity for complete graphs") {
      Position(Set(
        "Delaware",
        "Florida",
        "Georgia"
      ), None) shouldBe 'N

      Position(Set(
        "New Hampshire",
        "New Jersey",
        "New Mexico",
        "New York",
      ), None) shouldBe 'P

      Position(Set(
        "Rhode Island",
        "South Carolina",
        "South Dakota",
        "Tennessee",
        "Texas"
      ), None) shouldBe 'N

      Position(Set(
        "Illinois",
        "Indiana",
        "Missouri",
        "Texas",
        "Kentucky"
      ), None) shouldBe 'N
    }

    it("can handle more complex scenarios") {
      Position(Set(
        "Alabama",
        "Connecticut",
        "Kentucky",
        "Minnesota"
      ), None) shouldBe 'P

      Position(Set(
        "Arkansas",
        "Hawaii",
        "New Jersey",
        "Ohio",
      ), None) shouldBe 'P

      Position(Set(
        "Oregon",
        "Pennsylvania",
        "Utah",
        "Wyoming"
      ), None) shouldBe 'P
    }
  }
}
