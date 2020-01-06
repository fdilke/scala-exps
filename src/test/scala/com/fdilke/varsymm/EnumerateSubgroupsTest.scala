package com.fdilke.varsymm

import org.scalatest.funspec.AnyFunSpec

import scala.language.postfixOps
import org.scalatest.matchers.should.Matchers._

class EnumerateSubgroupsTest extends AnyFunSpec {
  describe("Enumerating subgroups") {
    it("of the trivial group works") {
      val trivialGroup = Permutation.group(1)
      trivialGroup.subgroups shouldBe Set(
        trivialGroup.trivialSubgroup
      )
    }

    it("of the 2-element group works") {
      val twoGroup = Permutation.group(2)

      twoGroup.subgroups shouldBe Set(
        twoGroup.trivialSubgroup,
        twoGroup.wholeGroup
      )
    }

    it("of cyclic groups works - at least orders are correct") {
      subgroupOrdersX(CyclicGroup(6)) shouldBe Seq(-1,-2,-3,-6)
      subgroupOrdersX(CyclicGroup(7)) shouldBe Seq(-1,-7)
      subgroupOrdersX(CyclicGroup(8)) shouldBe Seq(-1,-2,-4,-8)
    }

    it("of symmetric groups works") {
      subgroupOrdersX(Permutation.group(3)) shouldBe Seq(
        -1, 2, 2, 2, -3, -6
      )
    }

    it("of dihedral groups works") {
      subgroupOrdersX(DihedralGroup(12)) shouldBe Seq(
        -1, 2, 2, 2, 2, 2, 2, -2, -3, 4, 4, 4, -6, -6, -6, -12
      )
    }
  }

  // return the orders of a group's subgroups -
  // with the sign reversed if they're normal!
  // and ordered with the normal subgroups last for each order
  private def subgroupOrdersX[T](group: Group[T]): Seq[Int] =
    group.subgroups.toSeq.map { subgroup =>
      subgroup.order * (
        if (subgroup.isNormal) -1 else +1
      )
    } sortBy { orderX =>
      3 * Math.abs(orderX) - (
        if (orderX > 0) +1 else -1
      )
    }
}
