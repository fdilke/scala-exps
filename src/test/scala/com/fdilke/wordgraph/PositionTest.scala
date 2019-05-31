package com.fdilke.wordgraph

import org.scalatest.{FunSpec, Matchers}
import Matchers._

class PositionTest extends FunSpec {
  private val graph =
    new IndexedGraph[String](
      Set("foo", "bar", "baz", "corge", "fred") ++
        States.stateNames.toList,
      WordGraphCriterion.adjacent
    )

  describe("Allowable moves in a WordGraph position") {
    it("are all, if there is no previous word") {
      graph.positionFromNodes(
        Set("foo", "bar"),
        None
      ).allowedMovesAsNodes shouldBe Set(
        "foo", "bar"
      )
    }

    it("are all adjacent to the previous word, if present") {
      graph.positionFromNodes(
        Set("foo", "bar", "baz"),
        Some("corge")
      ).allowedMovesAsNodes shouldBe Set(
        "foo", "bar"
      )
    }
  }

  describe("Making a move in a WordGraph position") {
    it("is only allowed if it's in the set") {
      intercept[IllegalArgumentException] {
        graph.positionFromNodes(
          Set("foo", "bar"),
          Some("fred")
        ).moveFromNode("corge")
      }
    }

    it("is only allowed if it's adjacent") {
      intercept[IllegalArgumentException] {
        graph.positionFromNodes(
          Set("foo", "bar", "corge"),
          Some("baz")
        ).moveFromNode("corge")
      }
    }

    it("removes the move from the set and stores it as previous") {
      graph.positionFromNodes(
        Set("foo", "bar", "corge"),
        Some("fred")
      ).moveFromNode("corge") shouldBe
        graph.positionFromNodes(
          Set("foo", "bar"),
          Some("corge")
        )

      graph.positionFromNodes(
        Set("foo", "bar", "corge"),
        None
      ).moveFromNode("corge") shouldBe
        graph.positionFromNodes(
          Set("foo", "bar"),
          Some("corge")
        )
    }
  }

  describe("Verifying WordGraph positions for P") {
    it("has the right basic properties") {
      graph.positionFromNodes(Set.empty, None) shouldBe 'P
      graph.positionFromNodes(Set("foo"), None) should not be 'P
      graph.positionFromNodes(Set("foo", "bar"), None) should not be 'P
      graph.positionFromNodes(Set("bar", "baz"), None) shouldBe 'P
    }

    it("depends only on parity for complete graphs") {
      graph.positionFromNodes(Set(
        "Delaware",
        "Florida",
        "Georgia"
      ), None) shouldBe 'N

      graph.positionFromNodes(Set(
        "New Hampshire",
        "New Jersey",
        "New Mexico",
        "New York",
      ), None) shouldBe 'P

      graph.positionFromNodes(Set(
        "Rhode Island",
        "South Carolina",
        "South Dakota",
        "Tennessee",
        "Texas"
      ), None) shouldBe 'N

      graph.positionFromNodes(Set(
        "Illinois",
        "Indiana",
        "Missouri",
        "Texas",
        "Kentucky"
      ), None) shouldBe 'N
    }

    it("can handle more complex scenarios") {
      graph.positionFromNodes(Set(
        "Alabama",
        "Connecticut",
        "Kentucky",
        "Minnesota"
      ), None) shouldBe 'P

      graph.positionFromNodes(Set(
        "Arkansas",
        "Hawaii",
        "New Jersey",
        "Ohio",
      ), None) shouldBe 'P

      graph.positionFromNodes(Set(
        "Oregon",
        "Pennsylvania",
        "Utah",
        "Wyoming"
      ), None) shouldBe 'P
    }
  }
}
