package com.fdilke.wordgraph

import org.scalatest.{FunSpec, Matchers}
import Matchers._

import scala.collection.mutable

class IndexedGraphTest extends FunSpec {

  describe("Indexed graphs") {
    it("create an index of their vertex sets") {
      val graph =
        IndexedGraph(
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
      IndexedGraph(
        Set("foo", "bar", "baz"),
        alwaysAdjacent
      ).size shouldBe 3
    }

    it("eagerly caches adjacency information") {
      val adjacencyHelper = new AdjacencyHelper
      adjacencyHelper.flagAdjacent("foo", "bar")
      adjacencyHelper.flagAdjacent("bar", "baz")
      val graph =
        IndexedGraph(
          Set("foo", "bar", "baz", "corge"),
          adjacencyHelper.isAdjacent
        )
      System.err.println(s"queriedAdj = ${adjacencyHelper.queriedAdjacencies}")
      adjacencyHelper.queriedAdjacencies.size shouldBe 6
      val foo = graph.indexOf("foo")
      val bar = graph.indexOf("bar")
      val baz = graph.indexOf("baz")
      graph.isAdjacent(foo, bar) shouldBe true
      graph.isAdjacent(foo, baz) shouldBe false
      adjacencyHelper.queriedAdjacencies.size shouldBe 6
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
      val edge = str1 -> str2
      adjacencies.add(edge)
    }

    def isAdjacent(str1: String, str2: String): Boolean = {
      val edge = str1 -> str2
      queriedAdjacencies.append(edge)
      adjacencies.contains(edge)
    }
  }
}

