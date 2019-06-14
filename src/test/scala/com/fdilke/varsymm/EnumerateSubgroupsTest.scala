package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class EnumerateSubgroupsTest extends FunSpec {
  describe("Enumerating subgroups") {
    it("of the trivial group works") {
      val trivialGroup = Permutation.group(1)
      EnumerateSubgroups(trivialGroup) shouldBe Seq(
        trivialGroup.trivialSubgroup
      )
    }
  }
}
