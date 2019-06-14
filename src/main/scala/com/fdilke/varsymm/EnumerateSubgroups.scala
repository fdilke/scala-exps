package com.fdilke.varsymm

object EnumerateSubgroups {
  def apply[T](group: Group[T]): Traversable[group.Subgroup] =
    Seq(
      group.trivialSubgroup
    )
}
