package com.fdilke.scala

import org.mockito.IdiomaticMockito
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers._

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.collection.mutable
import scala.language.postfixOps

class UsingCollections extends AnyFunSuite with IdiomaticMockito {
    test("iterables") {
      val it : Iterable[String] = new Iterable[String]() {
        def iterator: Iterator[String] = new Iterator[String] {
          var count = 8
          def hasNext: Boolean = count > 0
          def next(): String = { count -= 1; "hello" }
        }
      } // a list of 8 "hello"s
      assert(List.fill(8)("hello") === (
        for (text <- it) yield text
        ))
    }

  test("seqs") {
    val array = Array("one", "two", "three")
    val seq : Seq[String] = new Seq[String] {
      def length: Int = array.length
      def apply(idx: Int): String = array(idx)
      def iterator: Iterator[String] = array.iterator
    }
    assert(seq === array.toList)
  }

  test("arrays") {
    val array = Array("one", "two", "three")
    assert(array(1) === "two")
    array(1) = "xx"
    assert(array(1) === "xx")
  }

  test("list buffers") {
    var buf = new ListBuffer[String]
    buf += "wird"
    buf += "groB"
    buf = "Kind" +: buf
    buf = "das" +: buf
    assert(buf.toList ===
      List("das", "Kind", "wird", "groB"))
  }

  test("array buffers") {
    var buf = new ArrayBuffer[String]
    buf += "wird"
    buf += "groB"
    buf = "Kind" +: buf
    buf = "das" +: buf
    assert(buf.toArray ===
      Array("das", "Kind", "wird", "groB"))
  }

  test("queues") {
    val q = new mutable.Queue[String]
    q += "All"
    q ++= List("good", "men", "come")
    List("All", "good", "men", "come") foreach { word =>
      assert(word === q.dequeue)
    }
    assert(q isEmpty)
  }

  test("stacks") {
    // Officially deprecated in favour of List
    // with pop => _.tail, push => _ :: list, so:
    var stack = List[String]()
    stack = "Pop" :: stack
    stack = "on" :: stack
    stack = "Hop" :: stack
    List("Hop", "on", "Pop") foreach { word =>
      assert(word === stack.head)
      stack = stack.tail
    }
    assert(stack isEmpty)
  }

  test("strings as Seq[Char]") {
    def hasUpperCase(s : String) = s exists(_.isUpper)
    assert(hasUpperCase("e e cummings") === false)
    assert(hasUpperCase("Collected Poetry"))

    assert(("For Whom The Bell Tolls" filter (_.isUpper)
      ) === "FWTBT")
    assert("Hello"(2) === 'l')
    assert(("Hello" map (_.toUpper)) === "HELLO")
  }

  test("mutable sets") {
    val set = mutable.Set[String]()
    for (word <- "the cat sat on the mat".split(" ")) {
      set += word
    }
    assert(5 === set.size)
    assert(Set("cat","the","sat","on") === set - "mat")
    assert(Set("cat","sat","on") === set -- List("the","mat"))
    set.clear()
    assert(set isEmpty)
  }

  test("mutable maps") {
    val m = mutable.Map[String, Int]()
    m("x") = 7
    assert(m("x") === 7)
    m("x") = 8
    assert(m("x") === 8)
    m += "y" -> 9
    assert(m("y") === 9)
    assert(m.size === 2)
    m ++= List("z"->4, "w"->7)
    assert(m.size === 4)
    m --= List("x", "y")
    assert(m.size === 2)
    m -= "z"
    assert((m - "w") isEmpty)
  }

  test("sorted set") {
    val set = mutable.SortedSet[String]()
    set += "zachary"
    set ++= List("abel", "isaac")
    assert(set.toList ===
      List("abel", "isaac", "zachary"))
  }
}
