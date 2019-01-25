package com.fdilke.mottoes

import com.fdilke.mottoes.UniqueSolution._

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

  def isUniquelySolvable: Boolean = {
    println("ZZZ testing: " + this)
    this match {
      case basic: BasicForm => false
      case CompoundMultiaryForm(args, finalTgt) =>
        BinaryForm.canUniquelySolve(args, finalTgt)
    }
  }

  def size: Int
  def letters: Seq[Char]
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
  ) : Boolean = {
    println(s"c: ${args map { _.toString} mkString ", "} => $finalTgt")
    args hasUniqueSolution {
      case basic: BasicForm =>
        println(s"c: 1")
        basic == finalTgt
      case CompoundMultiaryForm(otherArgs, otherTgt) =>
        println(s"c: 2")
        (otherTgt == finalTgt) &&
          otherArgs.forall { arg =>
            println(s"c: 2 arg = $arg")
            if (arg == finalTgt) { // avoid infinite descent
              println(s"c: 2 avoiding infinite descent")
              throw new AbandonUniqueSearchException
            } else {
              println(s"c: 2 descending")
              canUniquelySolveSub(args, arg)
            }
          }
    }
  }

  def canUniquelySolveSub(
    args: Seq[MultiaryForm],
    tgt: MultiaryForm
  ) : Boolean = {
    println(s"cSub: ${args map { _.toString} mkString ", "} => $tgt")
    tgt match {
      case basic: BasicForm =>
        println(s"cSub: 1")
        canUniquelySolve(args, basic)
      case CompoundMultiaryForm(otherArgs, otherTgt) =>
        println(s"cSub: 2")
        canUniquelySolve(args ++ otherArgs, otherTgt)
    }
  }
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

  override def letters: Seq[Char] =
    args.flatMap { _.letters } :+ result.letter

  override def size: Int =
    args.map { _.size }.sum + 1

  override def toString: String =
    "(" + args.map { _.toString }.mkString(",") + ") >> " + result.toString
}