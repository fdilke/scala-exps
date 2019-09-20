package com.fdilke.varsymm

object RightCosetRepresentatives {
  def apply[G](
    group: Group[G]
  )(
    smallSubgroup: group.Subgroup,
    bigSubgroup: group.Subgroup
  ): Seq[G] = {
    val index: Int =
      bigSubgroup.order / smallSubgroup.order

    Iterator.iterate(
      Seq(group.unit) -> smallSubgroup.elements
    ) (Function.tupled { (reps, repsH) =>
      val newRep: G =
        bigSubgroup.elements.diff(
          repsH
        ).head
      val newRepH = smallSubgroup.elements.map { h =>
        group.multiply(newRep, h)
      }
      (reps :+ newRep) -> (repsH union newRepH)
    }).drop(index - 1).toSeq.head._1
  }
}

// experimental

object LeftCosetRepresentatives {
  def apply[G](
                group: Group[G]
              )(
                smallSubgroup: group.Subgroup,
                bigSubgroup: group.Subgroup
              ): Seq[G] =
    RightCosetRepresentatives(
      group
    )(
      smallSubgroup,
      bigSubgroup
    ) map
      group.invert
}
