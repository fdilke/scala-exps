package com.fdilke.varsymm

import scala.language.postfixOps

trait AnnotatedInclusion[
  L <: LatticeElement[L, I],
  I <: AnnotatedInclusion[L, I]
] {
  val upper: L
  val lower: L
}

trait LatticeElement[
  L <: LatticeElement[L, I],
  I <: AnnotatedInclusion[L, I]
] {
  val strictlyAbove: Set[I]
  val strictlyBelow: Set[I]
}

trait AnnotatedLattice[
  L <: LatticeElement[L, I],
  I <: AnnotatedInclusion[L, I]
] {
  val bottom: L
  val top: L
}

object AnnotatedSubgroupLattice {
  def apply[T](
    group: Group[T]
  ): AnnotatedLattice[
    group.AnnotatedSubgroup,
    group.AnnotatedSubgroupInclusion
  ] =
  new AnnotatedLattice[
    group.AnnotatedSubgroup,
    group.AnnotatedSubgroupInclusion
  ] {
    private val subgroups: Set[group.Subgroup] =
      group.subgroups

    val annotatedSubgroups: Map[
      group.Subgroup,
      LocalAnnotatedSubgroup
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
    ) extends group.AnnotatedSubgroup { thisAnnotatedSubgroup =>
      override lazy val strictlyAbove: Set[
        group.AnnotatedSubgroupInclusion
      ] =
        strictlyDifferentWith(
          _.contains(toSubgroup),
          above = true
        )

      override lazy val strictlyBelow: Set[
        group.AnnotatedSubgroupInclusion
      ] =
        strictlyDifferentWith(
          toSubgroup.contains(_),
          above = false
        )

      private def strictlyDifferentWith(
        criterion: group.Subgroup => Boolean,
        above: Boolean
      ): Set[
        group.AnnotatedSubgroupInclusion
      ] =
        subgroups.filter { subgroup: group.Subgroup =>
          subgroup != toSubgroup &&
          criterion(subgroup)
        } map { subgroup: group.Subgroup =>
          annotatedSubgroups(subgroup)
        } map { annotatedSubgroup: LocalAnnotatedSubgroup =>
          if (above)
            new LocalInclusion(thisAnnotatedSubgroup, annotatedSubgroup)
          else
            new LocalInclusion(annotatedSubgroup, thisAnnotatedSubgroup)
        }
    }

    class LocalInclusion(
      override val lower: LocalAnnotatedSubgroup,
      override val upper: LocalAnnotatedSubgroup
    ) extends group.AnnotatedSubgroupInclusion
  }
}

