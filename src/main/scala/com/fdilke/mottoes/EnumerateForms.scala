package com.fdilke.mottoes

import com.fdilke.mottoes.StandardLetters._

import scala.language.postfixOps

object EnumerateForms {
  def apply(
    length: Int,
    letterStart: Int
  ): Seq[MultiaryForm] = {
    println(s"VVV EnumerateForms($length, $letterStart)")
    length match {
      case 0 => Seq()
      case 1 => (0 to letterStart) map BasicForm
      case _ =>
        for {
          i <- 1 until length
          prefix <- EnumerateForms(i, letterStart)
          highest = BinaryForm(prefix.letters.max).index
          _ = println("prefix: " + prefix + " highest = " + highest)
          suffix <- EnumerateForms(length - i, highest + 1)
        } yield {
          CompoundBinaryForm(
            prefix.toBinary,
            suffix.toBinary
          ).toMultiary
        }
    }
  }
}
