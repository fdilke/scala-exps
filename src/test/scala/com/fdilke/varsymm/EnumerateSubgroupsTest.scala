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

    // TODO: fix
    ignore("of the 2-element group works") {
      val twoGroup = Permutation.group(2)
      val thing = EnumerateSubgroups(twoGroup)
      println("thing size = " + thing.size)
      val Seq(foo, bar): Seq[twoGroup.Subgroup] = thing
      println("foo = " + foo)
      println("bar= " + bar)
      println("foo size = " + foo.order)
      println("bar= size " + bar.order)

      EnumerateSubgroups(twoGroup).toSet shouldBe Set(
        twoGroup.trivialSubgroup,
        twoGroup.wholeGroup
      )
    }
  }
}
