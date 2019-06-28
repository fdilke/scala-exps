package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.language.postfixOps

class EnumerateSubgroupsTest extends FunSpec {
  describe("Enumerating subgroups") {
    it("of the trivial group works") {
      val trivialGroup = Permutation.group(1)
      EnumerateSubgroups(trivialGroup) shouldBe Set(
        trivialGroup.trivialSubgroup
      )
    }

    it("of the 2-element group works") {
      val twoGroup = Permutation.group(2)

      EnumerateSubgroups(twoGroup) shouldBe Set(
        twoGroup.trivialSubgroup,
        twoGroup.wholeGroup
      )
    }

    it("of cyclic groups works - at least orders are correct") {
      subgroupOrders(CyclicGroup(6)) shouldBe Seq(1,2,3,6)
      subgroupOrders(CyclicGroup(7)) shouldBe Seq(1,7)
      subgroupOrders(CyclicGroup(8)) shouldBe Seq(1,2,4,8)
    }

    it("of symmetric groups works") {
      subgroupOrders(Permutation.group(3)) shouldBe Seq(
        1, 2, 2, 2, 3, 6
      )
    }
  }

  private def subgroupOrders[T](group: Group[T]): Seq[Int] =
    EnumerateSubgroups(
      group
    ).toSeq.map {
      _.order
    } sorted
}
