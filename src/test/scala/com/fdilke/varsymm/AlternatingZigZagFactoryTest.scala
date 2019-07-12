package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.language.postfixOps

class AlternatingZigZagFactoryTest extends FunSpec {
  private val group = DihedralGroup(6)
  private val lattice = AnnotatedSubgroupLattice(group)
  private val generator: () => Int = { () => 0 }
  private val zzFactory = AlternatingZigZagFactory(lattice, generator)

  describe("an alternating zig zag factory") {
    it("provides an initial zig") {
      val zigzag = zzFactory.initialZig
      zigzag shouldBe 'zig
      zigzag.lower shouldBe lattice.bottom
      zigzag.upper shouldBe lattice.bottom.strictlyAbove.head
      sanityCheck(zigzag)
    }

    it("provides an initial zag") {
      val zigzag = zzFactory.initialZag
      zigzag shouldBe 'zag
      zigzag.upper shouldBe lattice.top
      zigzag.lower shouldBe lattice.top.strictlyBelow.head
      sanityCheck(zigzag)
    }
  }

  private def sanityCheck(zigzag: ZigZag[group.AnnotatedSubgroup]) =
    zigzag.upper.toSubgroup.contains(
      zigzag.lower.toSubgroup
    ) shouldBe true
}
