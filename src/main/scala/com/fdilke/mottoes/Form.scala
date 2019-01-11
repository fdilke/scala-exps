package com.fdilke.mottoes

sealed trait MultiaryForm {

}

sealed trait Form {
  def :>(tgt: Form): Form =
    CompoundForm(this, tgt)

  def size: Int
  def letters: Seq[Char]

  final def isCanonical: Boolean = {
    val firsts = Form.firstOccurrences(letters)
    firsts == (
      firsts.indices map BasicForm map { _.letter }
    )
  }

  def isUniquelySolvable: Boolean =
    ???
}

object Form {
  val intForA: Int =
    'A'.toInt

  def firstOccurrences[T](seq: Seq[T]): Seq[T] =
    seq.foldLeft(Seq.empty[T]) { (occurrences, next) =>
      if (occurrences.contains(next))
        occurrences
      else
        occurrences :+ next
    }

  def apply(char: Char): BasicForm =
    BasicForm(char.toInt - intForA)
}

final case class BasicForm(
  index: Int
) extends Form with MultiaryForm {
  def letter : Char =
    (Form.intForA + index).toChar

  override def toString : String =
    letter.toString

  override def size: Int =
    1

  override def letters: Seq[Char] =
    Seq(letter)

  def from(args: MultiaryForm*): CompoundMultiaryForm = {
    CompoundMultiaryForm(args, this)
  }
}

final case class CompoundForm(
  src: Form,
  tgt: Form
) extends Form {
  override def toString : String =
    "(" + src.toString + " :> " + tgt.toString + ")"

  override def size: Int =
    src.size + tgt.size

  override def letters: Seq[Char] =
    src.letters ++ tgt.letters
}

object StandardLetters {
  val A = Form('A')
  val B = Form('B')
  val C = Form('C')
  val D = Form('D')
}

final case class CompoundMultiaryForm(
  args: Seq[MultiaryForm],
  result: BasicForm
) extends MultiaryForm {
   require(args.nonEmpty)

  override def toString: String =
    "(" + args.map { _.toString }.mkString(",") + ") >> " + result.toString
}