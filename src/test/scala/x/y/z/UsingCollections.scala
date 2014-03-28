package x.y.z

import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.collection.mutable

/**
 * Author: fdilke
 */
class UsingCollections extends FunSuite with MockFactory {
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
    val stack = new mutable.Stack[String]
    stack push "Pop"
    stack push "on"
    stack push "Hop"
    List("Hop", "on", "Pop") foreach { word =>
      assert(word === stack.pop)
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
