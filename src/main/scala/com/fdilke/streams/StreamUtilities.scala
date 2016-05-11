package com.fdilke.streams

import scala.Function.tupled
import scala.collection.Iterator.iterate
import scala.language.postfixOps

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

  def sequencesOfLength[X](
    length: Int
  ) (
    base: X*
  ): Seq[Seq[X]] =
    if (length == 0)
      Seq(Seq())
    else
      for {
        tail <- sequencesOfLength(length - 1)(base :_*)
        head <- base
      } yield
        head +: tail

  def sequences[X](
    base: X*
  ): Stream[Seq[X]] =
    if (base isEmpty)
      Stream(Seq())
    else
      N flatMap { n =>
        sequencesOfLength(n)(base :_*).toStream
      }
}
