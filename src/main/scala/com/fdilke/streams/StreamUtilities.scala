package com.fdilke.streams

import scala.Function.tupled
import scala.collection.Iterator.iterate

object StreamUtilities {

  // Note, this only works for infinite streams

  private val N = iterate(0)(_ + 1).toStream

  val indexPairs =
    N flatMap { n =>
      (0 to n).toStream map { i =>
        i -> (n - i)
      }
    }

  def pairs[X](
    left: Stream[X],
    right: Stream[X]
  ): Stream[(X, X)] =
    indexPairs map tupled {
      (m, n) => (left(m), right(n))
    }
}
