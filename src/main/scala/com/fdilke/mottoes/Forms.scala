package com.fdilke.mottoes

sealed trait MultiaryForm {

}

sealed trait BinaryForm {
  def :>(tgt: BinaryForm): BinaryForm =
    CompoundBinaryForm(this, tgt)

  def size: Int
  def letters: Seq[Char]

  final def isCanonical: Boolean = {
    val firsts = BinaryForm.firstOccurrences(letters)
    firsts == (
      firsts.indices map BasicBinaryForm map { _.letter }
    )
  }

  def isUniquelySolvable: Boolean =
    ???
}

object BinaryForm {
  val intForA: Int =
    'A'.toInt

  def firstOccurrences[T](seq: Seq[T]): Seq[T] =
    seq.foldLeft(Seq.empty[T]) { (occurrences, next) =>
      if (occurrences.contains(next))
        occurrences
      else
        occurrences :+ next
    }

  def apply(char: Char): BasicBinaryForm =
    BasicBinaryForm(char.toInt - intForA)
}

final case class BasicBinaryForm(
  index: Int
) extends BinaryForm with MultiaryForm {
  def letter : Char =
    (BinaryForm.intForA + index).toChar

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

final case class CompoundBinaryForm(
                                     src: BinaryForm,
                                     tgt: BinaryForm
) extends BinaryForm {
  override def toString : String =
    "(" + src.toString + " :> " + tgt.toString + ")"

  override def size: Int =
    src.size + tgt.size

  override def letters: Seq[Char] =
    src.letters ++ tgt.letters
}

object StandardLetters {
  val A = BinaryForm('A')
  val B = BinaryForm('B')
  val C = BinaryForm('C')
  val D = BinaryForm('D')
}

final case class CompoundMultiaryForm(
  args: Seq[MultiaryForm],
  result: BasicBinaryForm
) extends MultiaryForm {
   require(args.nonEmpty)

  override def toString: String =
    "(" + args.map { _.toString }.mkString(",") + ") >> " + result.toString
}