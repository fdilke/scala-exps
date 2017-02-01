package com.fdilke.scala

import java.util.concurrent.{ExecutorService, Executors, Callable}
import java.util.concurrent.atomic.AtomicBoolean

//import com.twitter.util.{Future, Await, FutureTask}
import org.scalatest.FunSpec
import org.scalatest.Matchers._

class TwitterFuturesTests extends FunSpec {
  describe("Twitter futures") {
    // Placeholder. The Twitter futures library hasn't been ported
    // to Scala 2.1 as of January 2017, so it's unclear that it HAS a future
  }

/*
    it("can be executed asynchronously") {
      val called = new AtomicBoolean(false)

      val futureTask = FutureTask {
        called.set(true)
        2 * 3
      }
      val future: Future[Int] = futureTask
      val task: Runnable = futureTask
      called.get() shouldBe false

      val pool: ExecutorService = Executors.newFixedThreadPool(10)
      pool.execute(task)
      Await.result(future) shouldBe 6
    }

    it("...experiments with exceptions") {
      val failed = new AtomicBoolean(false)
      val exceptionFuture: Future[Unit] =
        Future.exception(
          new IllegalArgumentException
        )

      exceptionFuture.isDone shouldBe false
      intercept[IllegalArgumentException] {
        Await.result(exceptionFuture) shouldBe new IllegalArgumentException
      }
      exceptionFuture.onSuccess { _ =>
        fail("should not be executed")
      } onFailure { ex =>
        failed.set(true)
      }
      failed.get() shouldBe true
    }
*/
}
