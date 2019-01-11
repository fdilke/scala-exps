package com.fdilke.mottoes

sealed trait MultiaryForm {
  def toBinary: BinaryForm =
    this match {
      case basic: BasicForm =>  basic
      case CompoundMultiaryForm(args, finalTgt) =>
        args.map {
          _.toBinary
        }.foldRight(finalTgt : BinaryForm) {
            _ :> _
          }
        }

  def isUniquelySolvable: Boolean =
    this match {
      case basic: BasicForm => false
      case CompoundMultiaryForm(args, finalTgt) =>
        BinaryForm.canUniquelySolve(args, finalTgt)
    }
}

sealed trait BinaryForm {
  def :>(tgt: BinaryForm): BinaryForm =
    CompoundBinaryForm(this, tgt)

  def size: Int
  def letters: Seq[Char]

  final def isCanonical: Boolean = {
    val firsts = BinaryForm.firstOccurrences(letters)
    firsts == (
      firsts.indices map BasicForm map { _.letter }
    )
  }

  def toMultiary: MultiaryForm =
    this match {
      case basic: BasicForm => basic
      case CompoundBinaryForm(src, tgt)  =>
        tgt.toMultiary match {
          case basic: BasicForm => basic.from(src.toMultiary)
          case CompoundMultiaryForm(args, finalTarget) =>
            finalTarget.from(src.toMultiary +: args:_*)
        }
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

  def apply(char: Char): BasicForm =
    BasicForm(char.toInt - intForA)

  def canUniquelySolve(
    args: Seq[MultiaryForm],
    finalTgt: BasicForm
  ) : Boolean =
    args.count { case CompoundMultiaryForm(x, y) =>
      true
    } == 1
}

final case class BasicForm(
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

  override def isUniquelySolvable: Boolean =
    false
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
  result: BasicForm
) extends MultiaryForm {
   require(args.nonEmpty)

  override def toString: String =
    "(" + args.map { _.toString }.mkString(",") + ") >> " + result.toString
}