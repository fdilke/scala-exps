package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class AnnotatedSubgroupLatticeTest extends FunSpec {
  describe("The annotated subgroup lattice") {
    it("includes the relevant subgroups") {
      val group = CyclicGroup(2)
      val lattice = AnnotatedSubgroupLattice(group)

      lattice.bottom.toSubgroup shouldBe group.trivialSubgroup
      lattice.bottom.strictlyBelow shouldBe Set.empty
      lattice.bottom.strictlyAbove map {
        _.toSubgroup
      } shouldBe Set(
        group.wholeGroup
      )

      lattice.top.toSubgroup shouldBe group.wholeGroup
      lattice.top.strictlyAbove shouldBe Set.empty
      lattice.top.strictlyBelow map {
        _.toSubgroup
      } shouldBe Set(
        group.trivialSubgroup
      )
    }
  }
}
