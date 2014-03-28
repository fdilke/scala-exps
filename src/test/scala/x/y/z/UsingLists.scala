package x.y.z

import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory

/**
 * Author: fdilke
 */

object InsertionSort {
  def sort : List[Int] => List[Int] = {
    case List() => List()
    case x :: xs => insert(x)(sort(xs))
  }

  def insert(x : Int) : List[Int] => List[Int] = {
    case List() => List(x)
    case y :: ys => if (x <= y) x :: y :: ys
                    else y :: insert(x)(ys)
  }
}

class UsingLists extends FunSuite with MockFactory {
  test("pattern matching with lists") {
    val list = 2 :: 3 :: 5 :: Nil
    val f : List[Int] => Int = {
      case x :: xs => xs.length
      case _ => -1
    }
    assert(f(list) === 2)
    assert(f(list tail) === 1)
  }

  test("insertion sorting") {
    assert(List(1,3,5,7,9) ===
      InsertionSort.sort(
        List(3, 9, 7, 5, 1)
      ))
  }

  test("concatenating lists") {
    assert(List(1,3,5) ::: List(7,9) ===
           List(1,3,5,7,9))
  }

  test("writing an append function") {
    val list1 = List(1,3,5)
    val list2 = List(7,9)

    def append[T](xs : List[T], ys : List[T]) : List[T] = xs match {
      case List() => ys
      case x :: xl => x :: append(xl, ys)
    }

    assert(append(list1, list2) ===
      list1 ::: list2)
  }

  test("map and flatMap") {
    assert((1 until 5).flatMap( i => (1 until i).map (j => (i, j)))
     === List(2->1, 3->1, 3->2, 4->1, 4->2, 4->3
    ))
  }

  test("finding elements in a list") {
    def even(n : Int) : Boolean = n % 2 == 0

    assert( 6 === (
    List(1,3,5,6,7) find even match {
      case Some(x : Int) => x
      case None => -1
    }))
  }

  test("folding left and right") {
    val op = (a : String, b : String) => "[" + a + b + "]"
    val list = List("a", "b", "c")
    assert(("z" /: list)(op) === "[[[za]b]c]")
    assert((list :\ "z")(op) === "[a[b[cz]]]")
  }

  test("repetitious lists") {
    assert(List.fill(4)("h") === List("h","h","h","h"))
  }

  test("zipping and unzipping lists") {
    val list1 = List(1,2,3)
    val list2 = List("a","b","c")
    val zipped = list1 zip list2
    assert(zipped === List(1->"a", 2->"b", 3->"c"))
    assert(zipped.unzip == (list1, list2))
  }

  test("flatten and concat") {
    val list1 = List(1,2,3)
    val list2 = List(4,5)
    val list3 = List(6,7)
    assert(List(list1, list2, list3).flatten === List.range(1,8))
    assert(List.concat(list1, list2, list3) === List.range(1,8))
  }
}