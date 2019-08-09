package com.fdilke.varsymm

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

    new group.MarkTable {
      override val blocks: Seq[group.Block] =
        orderedBlocks

      override val marks: Seq[Seq[Int]] =
        Seq(Seq(1))
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
      ): Boolean = {
        val subgroup: group.Subgroup =
          block.subgroups.head

        block2.subgroups.exists { containingSubgroup =>
          containingSubgroup.contains(subgroup)
        }
      }
    }
}
