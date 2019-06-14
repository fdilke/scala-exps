package com.fdilke.varsymm

object EnumerateSubgroups {
  def apply[T](group: Group[T]): Seq[group.Subgroup] = {
    def allAboveUsingWithout(
      above: group.Subgroup,
      elementsToUse: Seq[T],
      elementsToExclude: Seq[T]
    ): Seq[group.Subgroup] =
      elementsToUse match {
        case Nil => Seq(above)
        case x +: rest =>
          if (above.elements.contains(x) ||
            elementsToExclude.contains(x)) {
            allAboveUsingWithout(above, rest, elementsToExclude)
          } else {
            val aboveWithX =
              group.generateSubgroup(x +: above.elements :_*)
            allAboveUsingWithout(aboveWithX, rest, elementsToExclude) ++
              allAboveUsingWithout(above, rest, x +: elementsToExclude )
          }
    }

    allAboveUsingWithout(
      group.trivialSubgroup,
      group.elements,
      Seq.empty
    )
  }

//    Seq(
//      group.trivialSubgroup
//    )

}
