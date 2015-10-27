package com.fdilke.scala

import java.util.concurrent.{ExecutorService, Executors, Callable}
import java.util.concurrent.atomic.AtomicBoolean

import com.twitter.util.{Future, Await, FutureTask}
import org.scalatest.FunSpec
import org.scalatest.Matchers._

class TwitterFuturesTests extends FunSpec {
  describe("Twitter futures") {
    it("can be executed asynchronously") {
      val called = new AtomicBoolean(false)

      val futureTask = new FutureTask[Int]({
        called.set(true)
        2 * 3
      })
      val future: Future[Int] = futureTask
      val task: Runnable = futureTask
      called.get() shouldBe false

      val pool: ExecutorService = Executors.newFixedThreadPool(10)
      pool.execute(task)
      Await.result(future) shouldBe 6
    }
  }
}
