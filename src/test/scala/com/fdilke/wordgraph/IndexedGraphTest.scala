package com.fdilke.wordgraph

import org.scalatest.{FunSpec, Matchers}
import Matchers._

class IndexedGraphTest extends FunSpec {
  private val graph = IndexedGraph(Set("foo", "bar", "baz"))

  describe("Indexed graphs") {
    it("create an index of their vertex sets") {
      val foo = graph.indexOf("foo")
      val bar = graph.indexOf("bar")
      val baz = graph.indexOf("baz")
      Set(foo, bar, baz) shouldBe Set(0, 1, 2)
      graph.value(foo) shouldBe "foo"
      graph.value(bar) shouldBe "bar"
      graph.value(baz) shouldBe "baz"
    }

    it("measure the size of the graph") {
      graph.size() shouldBe 3
    }
  }
}

