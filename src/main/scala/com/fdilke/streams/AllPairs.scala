package com.fdilke.streams

import scala.Function.tupled
import scala.collection.Iterator.iterate

object AllPairs {

  // Note, this only works for infinite streams

  private val N = iterate(0)(_ + 1).toStream

  val indices =
    N flatMap { n =>
      (0 to n).toStream map { i =>
        i -> (n - i)
      }
    }

  def apply[X](stream: Stream[X]): Stream[(X, X)] =
    indices map tupled {
      (m, n) => (stream(m), stream(n))
    }
}
