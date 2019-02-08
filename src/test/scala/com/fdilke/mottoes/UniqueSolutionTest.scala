package com.fdilke.mottoes

import com.fdilke.mottoes.StandardLetters._
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import FormMatchers._
import UniqueSolution._

import scala.collection.mutable
import scala.collection.mutable.MutableList

class UniqueSolutionTest extends FunSpec {

  private val candidates: Seq[Int] =
    Seq(1, 2, 3)

  private def solveWith(criterion: Int => Boolean) =
    candidates.hasUniqueSolution { case x => criterion(x) }

  describe("The search for a unique solution") {
    it("will detect a unique solution, if available") {
      solveWith {
        _ == 2
      } shouldBe true
    }

    it("will detect when there are multiple solutions") {
      solveWith {
        _ > 1
      } shouldBe false
    }

    it("will detect when there is no solution") {
      solveWith {
        _ < 0
      } shouldBe false
    }

    it("will end as soon as we know the solution isn't unique") {
      val seen: mutable.MutableList[Int] =
        new mutable.MutableList[Int]

      solveWith { candidate =>
        seen += candidate
        candidate > 0
      } shouldBe false
      seen shouldBe Seq(1, 2)
    }

    it("will be abandoned if the criterion signals it") {
      val seen: mutable.MutableList[Int] =
        new mutable.MutableList[Int]

      solveWith { candidate =>
        seen += candidate
        if (candidate == 2)
          throw new AbandonUniqueSearchException
        candidate > 0
      } shouldBe false
      seen shouldBe Seq(1, 2)
    }
  }

  private def checkWith(criterion: Int => Boolean) =
    candidates.checkUniqueSolution { case x => criterion(x) }

  describe("The checking of unique solutions") {
    it("will detect a unique solution, if available") {
      checkWith {
        _ == 2
      } shouldBe Some(2)
    }

    it("will detect when there are multiple solutions") {
      checkWith {
        _ > 1
      } shouldBe None
    }

    it("will detect when there is no solution") {
      checkWith {
        _ < 0
      } shouldBe None
    }

    it("will end as soon as we know the solution isn't unique") {
      val seen: mutable.MutableList[Int] =
        new mutable.MutableList[Int]

      checkWith { candidate =>
        seen += candidate
        candidate > 0
      } shouldBe None
      seen shouldBe Seq(1, 2)
    }
  }
}
