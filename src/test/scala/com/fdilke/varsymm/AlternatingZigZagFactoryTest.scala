package com.fdilke.varsymm

import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.language.postfixOps

class AlternatingZigZagFactoryTest extends FunSpec {
  private val group = DihedralGroup(6)
  private val lattice = group.subgroupLattice
  private val generator: () => Int = { () => 0 }
  private val zzFactory =
    new AlternatingZigZagFactory(lattice, generator)

  describe("an alternating zig zag factory") {
    it("provides an initial zig") {
      val zigzag = zzFactory.initialZig
      zigzag shouldBe 'zig
      zigzag.lower shouldBe lattice.bottom
      zigzag.upper.toSubgroup shouldBe lattice.bottom.strictlyAbove.toSeq(0).upper.toSubgroup
      sanityCheck(zigzag)
    }

    it("provides an initial zag") {
      val zigzag = zzFactory.initialZag
      zigzag shouldBe 'zag
      zigzag.upper shouldBe lattice.top
      zigzag.lower.toSubgroup shouldBe lattice.top.strictlyBelow.toSeq(0).lower.toSubgroup
      sanityCheck(zigzag)
    }

    it("turns zigs into zags and vice versa") {
      val zigzags: Seq[ZigZag[group.AnnotatedSubgroup]] =
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
      val zagzigs: Seq[ZigZag[group.AnnotatedSubgroup]] =
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

  private def sanityCheck(zigzag: ZigZag[group.AnnotatedSubgroup]) =
    zigzag.upper.toSubgroup.contains(
      zigzag.lower.toSubgroup
    ) shouldBe true

  private def zigOrZag(index: Int): Symbol =
    if (index % 2 == 0)
      'zig
    else
      'zag

  private def zagOrZig(index: Int): Symbol =
    zigOrZag(index + 1)

  private def checkContinuation(
    zigOrZag: ZigZag[group.AnnotatedSubgroup],
    zagOrZig: ZigZag[group.AnnotatedSubgroup]
  ) =
    if (zigOrZag.isZig)
      checkZigThenZag(zigOrZag, zagOrZig)
    else
      checkZagThenZig(zigOrZag, zagOrZig)

  private def checkZigThenZag(
    zig: ZigZag[group.AnnotatedSubgroup],
    zag: ZigZag[group.AnnotatedSubgroup]
  ) {
    zag shouldBe 'zag
    zag.upper shouldBe zig.upper
  }

  private def checkZagThenZig(
    zag: ZigZag[group.AnnotatedSubgroup],
    zig: ZigZag[group.AnnotatedSubgroup]
  ) {
    zig shouldBe 'zig
    zig.lower shouldBe zag.lower
  }
}
