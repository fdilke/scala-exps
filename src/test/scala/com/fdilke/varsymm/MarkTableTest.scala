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
  }

}
