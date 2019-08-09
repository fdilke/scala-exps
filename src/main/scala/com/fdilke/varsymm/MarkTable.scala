package com.fdilke.varsymm

// Notation and conventions from here:
// https://en.wikipedia.org/wiki/Burnside_ring

object MarkTable {

  def apply[T](group: Group[T]): group.MarkTable = {
    implicit val ordering = blockOrdering(group)
    val unorderedBlocks: Set[group.Block] =
      group.subgroups.map { subgroup =>
        group.Block(
          group.elements.map {
            subgroup ^ _
          }
        )
      }
    val orderedBlocks =
      TopologicalSort(unorderedBlocks.toSeq)

    def mark(
      k: group.Subgroup,
      h: group.Subgroup
    ): Int = {
      val reps: Seq[T] =
        RightCosetRepresentatives(group)(
          k,
          group.wholeGroup
        )
      reps.count { g =>
        k.contains(h ^ g)
      }
    }

    new group.MarkTable {
      override val blocks: Seq[group.Block] =
        orderedBlocks

      override val marks: Seq[Seq[Int]] = {
        val indexedBlockheads: Seq[(group.Subgroup, Int)] =
          orderedBlocks.map {
            _.subgroups.head
          }.zipWithIndex

        for {
          (k, i) <- indexedBlockheads
        } yield for {
          (h, j) <- indexedBlockheads if j <= i
        } yield mark(k, h)
      }
    }
  }

  private def blockOrdering[T](
    group: Group[T]
  ): PartialOrdering[group.Block] =
    new PartialOrdering[group.Block] {
      override def tryCompare(
        block: group.Block,
        block2: group.Block
      ): Option[Int] =
        ??? // not needed

      override def lteq(
        block: group.Block,
        block2: group.Block
      ): Boolean =
        if (false) { // conventional
          val subgroup: group.Subgroup =
            block.subgroups.head

          block2.subgroups.exists { containingSubgroup =>
            containingSubgroup.contains(subgroup)
          }
        } else // weaker criterion which seems to be used in practice
          block.order < block2.order
    }
}
