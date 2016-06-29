package com.fdilke.scala

import org.scalatest.{FreeSpec, Matchers}
import Matchers._

import scala.util.control.TailCalls._

class TailRecTest extends FreeSpec {
  "Tail recursive computations encode" - {
    "values" in {
      val completed: TailRec[Int] = done(3)
      completed.result shouldBe 3
    }

    "functions" in {
      val toDo: TailRec[Int] =
        tailcall {
          done(2 * 3)
        }
      toDo.result shouldBe 6
    }

    "chained calculations" in {
      val toDo: TailRec[Int] =
        tailcall {
          done(2 * 3)
        }
      val chainedCalc: TailRec[Int] =
        toDo.flatMap { result =>
          tailcall {
            done(result + 1)
          }
        }
      chainedCalc.result shouldBe 7
    }

    "chained calculations in a for comprehension" in {
      val doMultiply: TailRec[Int] =
        tailcall {
          done(5 * 7)
        }
      val doSum: TailRec[Int] =
        tailcall {
          done(2 + 8)
        }
      val chainedCalc: TailRec[Int] =
        for {
          product <- doMultiply
          sum <- doSum
        } yield {
          product - sum
        }
      chainedCalc.result shouldBe 25
    }

    "corecursive computations" in {
      def isEven(num: Int): TailRec[Boolean] =
        num match {
          case 0 => done(true)
          case _ =>
            tailcall {
              isOdd(num - 1)
            }
        }

      def isOdd(num: Int): TailRec[Boolean] =
        num match {
          case 1 => done(true)
          case _ =>
            tailcall {
              isEven(num - 1)
            }
        }

      isOdd(5).result shouldBe true
    }
  }
}
