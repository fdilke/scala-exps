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

  def isCanonical: Boolean = {
    val firsts = Form.firstOccurrences(letters)
    firsts == (
      firsts.indices map BasicForm map { _.letter }
      )
  }

  def isUniquelySolvable: Boolean =
    this match {
      case _: BasicForm => false
      case CompoundMultiaryForm(args, finalTgt) =>
        Form.canUniquelySolve(args, finalTgt, Seq.empty)
    }

  def size: Int
  def letters: Seq[Char]
  def ::(other: MultiaryForm): MultiaryForm
}

sealed trait BinaryForm {
  def :>(tgt: BinaryForm): BinaryForm =
    CompoundBinaryForm(this, tgt)

  def size: Int
  def letters: Seq[Char]

  def isCanonical: Boolean = {
    val firsts = Form.firstOccurrences(letters)
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

  def canUniquelySolve(
    args: Seq[MultiaryForm],
    tgt: MultiaryForm,
    unreachables: Seq[MultiaryForm]
  ) : Boolean = {
//    println(s"caUniquelySolve($args, $tgt, $unreachables)")
    tgt match {
      case CompoundMultiaryForm(innerArgs, innerTgt) =>
        canUniquelySolve(args ++ innerArgs, innerTgt, unreachables)
      case basic: BasicForm =>
        args hasUniqueSolution {
          case basicArg: BasicForm =>
            basicArg == basic
          case CompoundMultiaryForm(innerArgs, innerTgt) =>
            (innerTgt == basic) &&
              innerArgs.forall { arg =>
//                println(s"potential infinite descent: args/tgt/basic/innerArgs/innerTgt = $args/$tgt/$basic/$innerArgs/$innerTgt")
                !(unreachables.contains(basic)) &&
                  canUniquelySolve(args, arg, unreachables :+ basic)
              }
        }
    }
  }
}

final case class BasicForm(
  index: Int
) extends BinaryForm with MultiaryForm {
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

  override def ::(prefix: MultiaryForm): MultiaryForm =
    this from prefix

  override def isUniquelySolvable: Boolean =
    false

  override def isCanonical: Boolean =
    super.isCanonical
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
  val A = Form('A')
  val B = Form('B')
  val C = Form('C')
  val D = Form('D')
  val E = Form('E')
  val F = Form('F')
}

final case class CompoundMultiaryForm(
  args: Seq[MultiaryForm],
  result: BasicForm
) extends MultiaryForm {
  override def ::(prefix: MultiaryForm): MultiaryForm =
    CompoundMultiaryForm(prefix +: args, result)

  require(args.nonEmpty)

  override def letters: Seq[Char] =
    args.flatMap { _.letters } :+ result.letter

  override def size: Int =
    args.map { _.size }.sum + 1

  override def toString: String =
    "(" + args.map { _.toString }.mkString(",") + ") >> " + result.toString
}