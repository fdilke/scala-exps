package com.fdilke.varsymm

import org.scalatest.matchers.should.Matchers._
import org.scalatest.funspec.AnyFunSpec

class AnnotatedSubgroupLatticeTest extends AnyFunSpec {
  describe("The annotated subgroup lattice") {
    it("includes the relevant subgroups") {
      val group = CyclicGroup(2)
      val lattice = group.subgroupLattice

      lattice.bottom.toSubgroup shouldBe group.trivialSubgroup
      lattice.bottom.strictlyBelow shouldBe Set.empty
      lattice.bottom.strictlyAbove map {
        _.lower.toSubgroup
      } shouldBe Set(
        group.trivialSubgroup
      )
      lattice.bottom.strictlyAbove map {
        _.upper.toSubgroup
      } shouldBe Set(
        group.wholeGroup
      )

      lattice.top.toSubgroup shouldBe group.wholeGroup
      lattice.top.strictlyAbove shouldBe Set.empty
      lattice.top.strictlyBelow map {
        _.lower.toSubgroup
      } shouldBe Set(
        group.trivialSubgroup
      )
      lattice.top.strictlyBelow map {
        _.upper.toSubgroup
      } shouldBe Set(
        group.wholeGroup
      )
    }
  }
}
