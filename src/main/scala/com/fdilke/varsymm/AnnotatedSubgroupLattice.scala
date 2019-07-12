package com.fdilke.varsymm

import scala.language.postfixOps

trait LatticeElement[L <: LatticeElement[L]] {
  val strictlyAbove: Set[L]
  val strictlyBelow: Set[L]
}

trait AnnotatedLattice[L <: LatticeElement[L]] {
  val bottom: L
  val top: L
}

object AnnotatedSubgroupLattice {
  def apply[T](
    group: Group[T]
  ): AnnotatedLattice[group.AnnotatedSubgroup] =
  new AnnotatedLattice[group.AnnotatedSubgroup] {
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
}

