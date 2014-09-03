package com.fdilke.scala

import scala.collection.mutable.ArrayBuffer
import org.junit.{Assert, Test}
import Assert._
import IntQueueTestHelper._
import scala.language.postfixOps

abstract class IntQueue {
  def get : Int
  def put(x : Int)
  def isEmpty : Boolean
}

class BasicIntQueue extends IntQueue {
  private val buffer = new ArrayBuffer[Int]
  def get = buffer.remove(0)
  def put(x : Int) { buffer += x }
  def isEmpty : Boolean = buffer.isEmpty
}

trait Doubling extends IntQueue {
  abstract override def put(x : Int) { super.put(2 * x) }
}

trait Incrementing extends IntQueue {
  abstract override def put(x : Int) { super.put(1 + x) }
}

trait Filtering extends IntQueue {
  abstract override def put(x : Int) { if (x >= 0) super.put(x) }
}

object IntQueueTestHelper {
  def addingTo(queue : IntQueue, values : Int*) = {
    values foreach queue.put
    new IntQueueTestHelper(queue)
  }
}

class IntQueueTestHelper(queue : IntQueue) {
  def gives(outputs : Int*) {
    outputs foreach { assertEquals(_, queue get) }
    assertTrue(queue isEmpty)
  }
}

class IntQueueTest {
  @Test def storesValuesLIFO() {
    val basicQueue = new BasicIntQueue

    basicQueue put 2
    assertFalse(basicQueue isEmpty)
    basicQueue put(-3)
    assertFalse(basicQueue isEmpty)

    assertEquals(2, basicQueue get)
    assertEquals(-3, basicQueue get)
    assertTrue(basicQueue isEmpty)
  }

  @Test(expected = classOf[Exception])
  def exceptionOnQueryingEmptyQueue() {
    val basicQueue = new BasicIntQueue

    basicQueue.get
  }

  @Test def doubling() {
    addingTo(new BasicIntQueue with Doubling,
             2, -3
    ) gives(4, -6)
  }

  @Test def incrementing() {
    addingTo(new BasicIntQueue with Incrementing,
             2, -3
    ) gives(3, -2)
  }

  @Test def filtering() {
    addingTo(new BasicIntQueue with Filtering,
            -2, 3
    ) gives 3
  }

  @Test def incrementingThenDoubling() {
    addingTo(new BasicIntQueue with Doubling with Incrementing,
             2,-3
    ) gives(6, -4)
  }

  @Test def doublingThenIncrementing() {
    addingTo(new BasicIntQueue with Incrementing with Doubling,
             2, -3
    ) gives(5, -5)
  }

  @Test def incrementingThenFiltering() {
    addingTo(new BasicIntQueue with Filtering with Incrementing,
             -2,2,-1
    ) gives(3,0)
  }

  @Test def filteringThenIncrementing() {
    addingTo(new BasicIntQueue with Incrementing with Filtering,
             -2,2,-1
    ) gives 3
  }

  @Test def doublingThenFiltering() {
    addingTo(new BasicIntQueue with Filtering with Doubling,
             -2,2,-1
    ) gives 4
  }

  @Test def filteringThenDoubling() {
    addingTo(new BasicIntQueue with Doubling with Filtering,
             -2,2,-1
    ) gives 4
  }
}


