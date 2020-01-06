package com.fdilke.varsymm

import org.scalatest.matchers.should.Matchers._
import org.scalatest.funspec.AnyFunSpec

import scala.language.postfixOps

class AlternatingZigZagFactoryTest extends AnyFunSpec {
  private val group = DihedralGroup(6)
  private val lattice = group.subgroupLattice
  private val generator: () => Int = { () => 0 }
  private val zzFactory =
    new AlternatingZigZagFactory(lattice, generator)

  describe("an alternating zig zag factory") {
    it("provides an initial zig") {
      val zigzag = zzFactory.initialZig
      zigzag shouldBe 'zig
      zigzag.inclusion.lower shouldBe lattice.bottom
      zigzag.inclusion.upper.toSubgroup shouldBe lattice.bottom.strictlyAbove.toSeq(0).upper.toSubgroup
      zigzag.next shouldBe zigzag.inclusion.lower
      sanityCheck(zigzag)
    }

    it("provides an initial zag") {
      val zigzag = zzFactory.initialZag
      zigzag shouldBe 'zag
      zigzag.inclusion.upper shouldBe lattice.top
      zigzag.inclusion.lower.toSubgroup shouldBe lattice.top.strictlyBelow.toSeq(0).lower.toSubgroup
      zigzag.next shouldBe zigzag.inclusion.upper
      sanityCheck(zigzag)
    }

    it("turns zigs into zags and vice versa") {
      val zigzags: Seq[ZigZag[group.AnnotatedSubgroup, group.AnnotatedSubgroupInclusion]] =
        Iterator.iterate(
          zzFactory.initialZig
        ) {
          zzFactory(_)
        } take 20 toSeq

      for {
        (zigzag, index) <- zigzags.zipWithIndex
      } {
        zigzag shouldBe zigOrZag(index)
        sanityCheck(zigzag)
      }

      for {
        (zigOrZag, zagOrZig) <- zigzags zip zigzags.tail
      }
        checkContinuation(zigOrZag, zagOrZig)
    }

    it("turns zags into zigs and vice versa") {
      val zagzigs: Seq[ZigZag[group.AnnotatedSubgroup, group.AnnotatedSubgroupInclusion]] =
        Iterator.iterate(
          zzFactory.initialZag
        ) {
          zzFactory(_)
        } take 20 toSeq

      for {
        (zigzag, index) <- zagzigs.zipWithIndex
      } {
        zigzag shouldBe zagOrZig(index)
        sanityCheck(zigzag)
      }

      for {
        (zigOrZag, zagOrZig) <- zagzigs zip zagzigs.tail
      }
        checkContinuation(zigOrZag, zagOrZig)
    }
  }

  private def sanityCheck(zigzag: ZigZag[group.AnnotatedSubgroup, group.AnnotatedSubgroupInclusion]) {
    zigzag.inclusion.upper.toSubgroup.contains(
      zigzag.inclusion.lower.toSubgroup
    ) shouldBe true
  }

  private def zigOrZag(index: Int): Symbol =
    if (index % 2 == 0)
      'zig
    else
      'zag

  private def zagOrZig(index: Int): Symbol =
    zigOrZag(index + 1)

  private def checkContinuation(
    zigOrZag: ZigZag[group.AnnotatedSubgroup, group.AnnotatedSubgroupInclusion],
    zagOrZig: ZigZag[group.AnnotatedSubgroup, group.AnnotatedSubgroupInclusion]
  ) =
    if (zigOrZag.isZig)
      checkZigThenZag(zigOrZag, zagOrZig)
    else
      checkZagThenZig(zigOrZag, zagOrZig)

  private def checkZigThenZag(
    zig: ZigZag[group.AnnotatedSubgroup, group.AnnotatedSubgroupInclusion],
    zag: ZigZag[group.AnnotatedSubgroup, group.AnnotatedSubgroupInclusion]
  ) {
    zag shouldBe 'zag
    zag.inclusion.upper shouldBe zig.inclusion.upper
  }

  private def checkZagThenZig(
    zag: ZigZag[group.AnnotatedSubgroup, group.AnnotatedSubgroupInclusion],
    zig: ZigZag[group.AnnotatedSubgroup, group.AnnotatedSubgroupInclusion]
  ) {
    zig shouldBe 'zig
    zig.inclusion.lower shouldBe zag.inclusion.lower
  }
}
