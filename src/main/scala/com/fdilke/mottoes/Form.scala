package com.fdilke.mottoes

sealed trait Form {
  def :>(tgt: Form): Form =
    CompoundForm(this, tgt)
}

object Form {
  val intForA: Int =
    'A'.toInt

  def apply(char: Char): Form =
    BasicForm(char.toInt - intForA)
}

final case class BasicForm(
  index: Int
) extends Form {
  override def toString : String =
    (Form.intForA + index).toChar.toString
}

final case class CompoundForm(
  src: Form,
  tgt: Form
) extends Form {
  override def toString : String =
    "(" + src.toString + " :> " + tgt.toString + ")"
}

object StandardLetters {
  val A = Form('A')
  val B = Form('B')
  val C = Form('C')
}
