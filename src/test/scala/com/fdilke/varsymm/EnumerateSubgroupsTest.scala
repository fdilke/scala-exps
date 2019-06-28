package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

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

//    it("of the cyclic group works") {
//      val twoGroup = CyclicGroup(6)
//
//      EnumerateSubgroups(twoGroup) shouldBe Set(
//        twoGroup.trivialSubgroup,
//        twoGroup.wholeGroup
//      )
//    }
  }
}
