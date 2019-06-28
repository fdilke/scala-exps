package com.fdilke.varsymm

object EnumerateSubgroups {
  def apply[T](group: Group[T]): Set[group.Subgroup] = {
    def allAboveUsingWithout(
      above: group.Subgroup,
      elementsToUse: Seq[T],
      elementsToExclude: Seq[T]
    ): Set[group.Subgroup] =
      elementsToUse match {
        case Nil => Set(above)
        case x +: rest =>
          if (above.elements.contains(x) ||
            elementsToExclude.contains(x)
          ) {
            allAboveUsingWithout(above, rest, elementsToExclude)
          } else {
            val aboveWithX =
              group.generateSubgroup(above.elements + x)
            allAboveUsingWithout(aboveWithX, rest, elementsToExclude) ++
              allAboveUsingWithout(above, rest, x +: elementsToExclude )
          }
    }

    allAboveUsingWithout(
      group.trivialSubgroup,
      group.elements.toSeq,
      Seq.empty
    )
  }

//    Seq(
//      group.trivialSubgroup
//    )

}
