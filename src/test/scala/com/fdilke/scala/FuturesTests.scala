package com.fdilke.scala

import java.util.concurrent.atomic.AtomicBoolean

import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionException, Future}

class FuturesTests extends FunSpec {
  private val ASAP: FiniteDuration = 200 millis

  describe("Futures") {

    it("can be used to wait for a value we already have") {
      val f2: Future[Int] = Future.successful(2)
      Await.result(f2, ASAP) shouldBe 2
    }

    it("can be used to wait for a successful operation") {
      val done = new AtomicBoolean(false)
      val f2: Future[Int] = Future {
        done.set(true)
        2
      }
      done.get shouldBe false
      Await.result(f2, ASAP) shouldBe 2
      done.get shouldBe true
    }

    it("can be used to wait for a failed operation") {
      val f2 = Future { ??? }
      intercept[ExecutionException] {
        Await.result(f2, ASAP)
      }.getCause shouldBe a[NotImplementedError]
    }

    it("can collect the results of multiple operations") {
      val deferredCalc: Future[Seq[Any]] = Future.sequence(Seq(
        Future { 2 },
        Future { "xx" }
      ))
      Await.result(deferredCalc, ASAP) shouldBe Seq(2, "xx")
    }

    it("can't quite collect the results of multiple operations when they fail") {
      val deferredCalc: Future[Seq[Int]] = Future.sequence(Seq(
        Future { 2 },
        Future { ??? }
      ))
      intercept[ExecutionException] {
        Await.result(deferredCalc, ASAP)
      }.getCause shouldBe a[NotImplementedError]
    }
  }
}
