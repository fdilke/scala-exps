package com.fdilke.varsymm

import scala.language.postfixOps

case class AnnotatedSubgroupLattice[T](
  group: Group[T]
) {
  private val subgroups: Set[group.Subgroup] =
    EnumerateSubgroups(group)

  val annotatedSubgroups: Map[
    group.Subgroup,
    group.AnnotatedSubgroup
  ] = subgroups.map { subgroup =>
    subgroup -> new LocalAnnotatedSubgroup(subgroup)
  } toMap

  val bottom: group.AnnotatedSubgroup =
    new LocalAnnotatedSubgroup(
      group.trivialSubgroup
    )

  val top: group.AnnotatedSubgroup =
    new LocalAnnotatedSubgroup(
      group.wholeGroup
    )

  class LocalAnnotatedSubgroup(
    override val toSubgroup: group.Subgroup
  ) extends group.AnnotatedSubgroup {
    override lazy val strictlyAbove: Set[
      group.AnnotatedSubgroup
    ] =
      strictlyDifferentWith {
        _.contains(toSubgroup)
      }

    override lazy val strictlyBelow: Set[
      group.AnnotatedSubgroup
    ] =
      strictlyDifferentWith {
        toSubgroup.contains(_)
      }

    private def strictlyDifferentWith(
      criterion: group.Subgroup => Boolean
    ): Set[
      group.AnnotatedSubgroup
    ] =
      subgroups.filter { subgroup: group.Subgroup =>
        subgroup != toSubgroup &&
        criterion(subgroup)
      } map { subgroup: group.Subgroup =>
        annotatedSubgroups(subgroup)
      }
  }
}

