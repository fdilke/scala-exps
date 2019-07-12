package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.language.postfixOps

class GroupTest extends FunSpec {
  describe("Built-in methods for Groups") {
    it("can calculate the subgroup generated by a subset") {
      val group: Group[Permutation] =
        Permutation.group(3)

      val transposition = Permutation(0, 2, 1)

      group.generateSubgroup() shouldBe group.trivialSubgroup

      group.generateSubgroup(
        transposition
      ).elements shouldBe Set(
        group.unit,
        transposition
      )

      val rotation = Permutation(1, 2, 0)

      group.generateSubgroup(
        transposition,
        rotation
      ).elements.toSet shouldBe
        group.elements.toSet
    }

    it("can tell if a group is cyclic") {
      CyclicGroup(9).isCyclic shouldBe true
      DihedralGroup(4).isCyclic shouldBe false
      Permutation.group(3).isCyclic shouldBe false
    }

    it("can tell if a group is abelian") {
      CyclicGroup(9).isAbelian shouldBe true
      DihedralGroup(4).isAbelian shouldBe true
      Permutation.group(3).isAbelian shouldBe false
    }

    describe("can compute the centre") {
      it("for cyclic groups") {
        val group = CyclicGroup(9)
        group.centre shouldBe group.wholeGroup
      }

      it("for symmetric groups") {
        val group = Permutation.group(4)
        group.centre shouldBe group.trivialSubgroup
      }

      it("for dihedral groups") {
        val group = DihedralGroup(4)
        group.centre shouldBe group.wholeGroup
      }

      it("for bigger dihedral groups") {
        DihedralGroup(6).centre.order shouldBe 1
        DihedralGroup(12).centre.order shouldBe 2
      }
    }
  }

  describe("can compute the order of elements") {
    it("for cyclic groups") {
      elementOrders(CyclicGroup(9)) shouldBe Seq(
        1, 3, 3, 9, 9, 9, 9, 9, 9
      )
    }
    it("for dihedral groups") {
      elementOrders(DihedralGroup(6)) shouldBe Seq(
        1, 2, 2, 2, 3, 3
      )
    }
  }

  private def elementOrders[T](group: Group[T]): Seq[Int] = {
    group.elements.toSeq map {
      group.orderOf
    } sorted
  }
}
