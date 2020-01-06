package com.fdilke.scala

import com.fdilke.runtime.RunTimeCompilation
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class RunTimeCompilationTest extends AnyFunSpec with RunTimeCompilation {
  describe("Run time compilation") {

    it("can verify what compiles and what doesn't") {
      "2 + 3" should compile
      "0 / 'a" should not(compile)
    }

    it("can use imports as a context") {
      "Pi" should not(compile)
      inContextOf("scala.math.Pi") {
        "Pi" should compile
      }
    }

    it("can use multiple imports") {
      val expression = "classOf[AbstractMap[_,_]].toString + classOf[Future[_]].toString"
      inContextOf("scala.collection.AbstractMap") {
        expression should not(compile)
      }
      inContextOf("scala.concurrent.Future") {
        expression should not(compile)
      }
      inContextOf("scala.collection.AbstractMap", "scala.concurrent.Future") {
        expression should compile
      }
    }
  }
}
