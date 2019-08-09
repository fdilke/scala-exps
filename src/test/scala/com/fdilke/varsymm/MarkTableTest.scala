package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class MarkTableTest extends FunSpec {

  describe("The mark table") {
    it("of the trivial group is as expected") {
      val group = CyclicGroup(1)
      val markTable = group.markTable
      markTable.blocks should have size 1
      markTable.blocks.head.subgroups shouldBe Set(group.wholeGroup)
      markTable.marks shouldBe Seq(Seq(1))
    }

    it("of a cyclic group C_6 is as expected") {
      val group = CyclicGroup(6)
      val markTable = group.markTable
      markTable.blocks should have size 4
      markTable.blocks.foreach { block =>
        block.subgroups should have size 1
      }
      markTable.blocks.head.subgroups shouldBe Set(group.trivialSubgroup)
      markTable.blocks(3).subgroups shouldBe Set(group.wholeGroup)
      markTable.orders shouldBe Seq(1, 2, 3, 6)
      markTable.marks shouldBe Seq(
        Seq(6),
        Seq(3, 3),
        Seq(2, 0, 2),
        Seq(1, 1, 1, 1),
      )
    }

    it("of the symmetric group S_3 is as expected") {
      val group = Permutation.group(3)
      val markTable = group.markTable
      markTable.blocks should have size 4
      markTable.blocks.foreach { block =>
        block.subgroups should have size (
          if (block.subgroups.head.order == 2)
            3
          else
            1
        )
      }
      markTable.blocks.head.subgroups shouldBe Set(group.trivialSubgroup)
      markTable.blocks(3).subgroups shouldBe Set(group.wholeGroup)
      markTable.orders shouldBe Seq(1, 2, 3, 6)
      markTable.marks shouldBe Seq(
        Seq(6),
        Seq(3, 1),
        Seq(2, 0, 2),
        Seq(1, 1, 1, 1),
      )
    }
  }
}
