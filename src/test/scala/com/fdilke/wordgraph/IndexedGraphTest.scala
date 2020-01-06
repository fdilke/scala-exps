package com.fdilke.wordgraph

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

import scala.collection.mutable

class IndexedGraphTest extends AnyFunSpec {

  describe("Indexed graphs") {
    it("create an index of their vertex sets") {
      val graph =
        new IndexedGraph(
          Set("foo", "bar", "baz"),
          alwaysAdjacent
        )
      val foo = graph.indexOf("foo")
      val bar = graph.indexOf("bar")
      val baz = graph.indexOf("baz")
      Set(foo, bar, baz) shouldBe Set(0, 1, 2)
      graph.value(foo) shouldBe "foo"
      graph.value(bar) shouldBe "bar"
      graph.value(baz) shouldBe "baz"
    }

    it("measure the size of the graph") {
      new IndexedGraph(
        Set("foo", "bar", "baz"),
        alwaysAdjacent
      ).size shouldBe 3
    }

    it("eagerly caches adjacency information") {
      val adjacencyHelper = new AdjacencyHelper
      adjacencyHelper.flagAdjacent("foo", "bar")
      adjacencyHelper.flagAdjacent("bar", "baz")
      val graph =
        new IndexedGraph(
          Set("foo", "bar", "baz", "corge"),
          adjacencyHelper.isAdjacent
        )
      adjacencyHelper.queriedAdjacencies.size shouldBe 6
      val foo = graph.indexOf("foo")
      val bar = graph.indexOf("bar")
      val baz = graph.indexOf("baz")
      graph.isAdjacent(foo, bar) shouldBe true
      graph.isAdjacent(foo, baz) shouldBe false
      adjacencyHelper.queriedAdjacencies.size shouldBe 6
    }

    it("tests reachability") {
      val adjacencyHelper = new AdjacencyHelper
      adjacencyHelper.flagAdjacent("foo", "bar")
      adjacencyHelper.flagAdjacent("bar", "baz")
      val graph =
        new IndexedGraph(
          Set("foo", "bar", "baz", "corge"),
          adjacencyHelper.isAdjacent
        )
      graph.reachableFromAsNodes(
        Set("foo", "bar", "baz", "corge"),
        "baz"
      ) shouldBe Set(
        "foo", "bar", "baz"
      )
    }

    it("tests reachability - a more complex example") {
      val adjacencyHelper = new AdjacencyHelper
      adjacencyHelper.flagAdjacent("foo", "bar")
      adjacencyHelper.flagAdjacent("bar", "baz")
      adjacencyHelper.flagAdjacent("baz", "boi")
      adjacencyHelper.flagAdjacent("baz", "quux")
      val graph =
        new IndexedGraph(
          Set("foo", "bar", "baz", "corge", "quux", "wurve" ,"boi"),
          adjacencyHelper.isAdjacent
        )
      graph.reachableFromAsNodes(
        Set("bar", "baz", "corge", "quux", "wurve" ,"boi"),
        "baz"
      ) shouldBe Set(
        "bar", "baz", "boi", "quux"
      )
    }
  }

  private def alwaysAdjacent(str1: String, str2: String) =
    true

  class AdjacencyHelper {
    val adjacencies: mutable.Set[(String, String)] =
      mutable.Set[(String, String)]()

    val queriedAdjacencies: mutable.Buffer[(String, String)] =
      mutable.Buffer[(String, String)]()

    def flagAdjacent(str1: String, str2: String): Unit = {
      adjacencies.add(str1 -> str2)
    }

    def isAdjacent(str1: String, str2: String): Boolean = {
      queriedAdjacencies.append(str1 -> str2)
      adjacencies.contains(str1 -> str2) ||
        adjacencies.contains(str2 -> str1)
    }
  }
}

