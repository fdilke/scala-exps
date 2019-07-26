package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class RightCosetRepresentativesTest extends FunSpec {
  private val group = DihedralGroup(6)

  describe("A set of right coset representatives") {
    it("is just 1 in the trivial case H=G") {
      RightCosetRepresentatives(group)(
        group.wholeGroup,
        group.wholeGroup
      ) shouldBe Seq(
        group.unit
      )
    }

    it("is just the group elements in the trivial case H=1") {
      val representatives =
        RightCosetRepresentatives(group)(
          group.trivialSubgroup,
          group.wholeGroup
        )
      representatives should not be(empty)
      representatives.head shouldBe group.unit
      representatives.toSet shouldBe group.elements
    }

    it("is sensible for the case G:H = 2") {
      val subgroup =
        group.generateSubgroup(
          DihedralSymmetry(3, reflect = false, 1)
        )
      val representatives =
        RightCosetRepresentatives(group)(
          subgroup,
          group.wholeGroup
        )
      representatives should have size 2
      representatives.head shouldBe group.unit
      subgroup.elements should not contain representatives(1)
    }

    it("is sensible for the case G:H = 3") {
      val subgroup =
        group.generateSubgroup(
          group.elements.find { x =>
            group.orderOf(x) == 2
          }.get
        )
      val representatives: Seq[DihedralSymmetry] =
        RightCosetRepresentatives(group)(
          subgroup,
          group.wholeGroup
        )
      representatives should have size 3
      representatives.head shouldBe group.unit
      val x = representatives(1)
      val y = representatives(2)
      def timesH(z: DihedralSymmetry): Set[DihedralSymmetry] =
        subgroup.elements.map { h =>
          group.multiply(z, h)
        }
      val xH = timesH(x)
      val yH = timesH(y)
      xH intersect yH shouldBe empty
      xH intersect subgroup.elements shouldBe empty
      yH intersect subgroup.elements shouldBe empty
    }
  }
}
