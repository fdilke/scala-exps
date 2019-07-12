package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.language.postfixOps

class AlternatingZigZagFactoryTest extends FunSpec {
  private val group = DihedralGroup(6)
  private val lattice = AnnotatedSubgroupLattice(group)
  private val generator: () => Int = { () => 1}
  private val zzFactory = AlternatingZigZagFactory(lattice, generator)

  describe("an alternating zig zag factory") {
    it("provides an initial zig") {
      val zigzag = zzFactory.initialZig
      zigzag shouldBe 'zig
    }
  }
}
