package com.fdilke.varsymm

// decided to NOT use Kahn's algorithm (see Wikipedia)
// but instead a simplification of it that works for posets

object TopologicalSort {
  def apply[T : PartialOrdering](
    list: Seq[T]
  ): Seq[T] =
    if (list.isEmpty)
      list
    else
      aLeast(list) match {
        case Some(least) =>
          least +: TopologicalSort(list.filterNot( _ == least))
        case None =>
          throw new IllegalArgumentException
      }

  private def aLeast[T : PartialOrdering](
    list: Seq[T]
  ): Option[T] = {
    val ordering: PartialOrdering[T] = implicitly

    list.find { t =>
      list.forall { s =>
        !ordering.lt(s, t)
      }
    }
  }
}
