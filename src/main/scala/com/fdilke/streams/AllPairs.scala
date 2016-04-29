package com.fdilke.streams

import scala.Function.tupled

object AllPairs {

  val indices =
    indicesFrom(0)

  private def indicesFrom(n: Int): Stream[(Int, Int)] =
    indicesSub(n) #::: indicesFrom(n + 1)

  private def indicesSub(n: Int) =
    (0 to n).toStream.map { i =>
      i -> (n - i)
    }

  def apply[X](stream: Stream[X]): Stream[(X, X)] =
    indices map tupled {
      (m, n) => (stream(m), stream(n))
    }
}
