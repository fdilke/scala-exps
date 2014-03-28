package x.y.z

import org.scalatest._
import org.junit.Assert
import Assert._

class PartialFunctionTests extends FunSpec with ShouldMatchers {

  describe("A partial function") {
    it("can be used to collect values from a list") {
      val list = List(Some("Kind"), None, Some("Dog"))
      list.collect {
        case Some(string) => string.length()
      } should be(List(4,3))
    }

    it("can be used to collect values from a map") {
      val map = Map(2->"hello", 3->"goodbye", 4->"xxx")
      map.collect {
        case ((n : Int), message) if n % 2 == 0 => s"$message+$message"
      } should be (List("hello+hello", "xxx+xxx"))
    }

    it("knows when it is undefined") {
      val partialFunc : PartialFunction[Int, String] = {
        case n : Int if n % 2 == 0 => "even"
      }
      assertFalse(partialFunc.isDefinedAt(1))
      assert(partialFunc.isDefinedAt(2))
    }
  }
}