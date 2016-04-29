package com.fdilke.scala

import com.fdilke.mottoes.ExpressionMatching._
import com.fdilke.mottoes.Expressions._
import com.fdilke.mottoes.Node._
import org.scalatest.FreeSpec
import org.scalatest.Matchers._

import scala.Function.tupled

class RecursiveStreamsTest extends FreeSpec {
  "Recursive streams" - {
    "can be used to calculate recurrences" in {
      lazy val fibs: Stream[Int] =
        0 #:: 1 #:: (
          fibs zip fibs.tail map tupled {
            _ + _
          }
        )

      fibs.take(8).toList shouldBe Seq(
        0, 1, 1, 2, 3, 5, 8, 13
      )
    }
  }
}
