package com.fdilke.mottoes

import com.fdilke.mottoes.UniqueSolution._

import scala.collection.mutable

object FormSolver {
  def apply(form: MultiaryForm): Option[Set[MultiaryForm]] =
    form match {
      case _: BasicForm => None
      case CompoundMultiaryForm(args, result) =>
        new FormSolver(
          args,
          result
        ).solve()
    }
}

class FormSolver(
  args: Seq[MultiaryForm],
  tgt: MultiaryForm
) {
  def canUniquelySolve(
    args: Seq[MultiaryForm],
    tgt: MultiaryForm,
    unreachables: Seq[MultiaryForm]
  ) : Option[Set[MultiaryForm]] = {
    //    println(s"caUniquelySolve($args, $tgt, $unreachables)")
    tgt match {
      case CompoundMultiaryForm(innerArgs, innerTgt) =>
        canUniquelySolve(args ++ innerArgs, innerTgt, unreachables)
      case basic: BasicForm =>
        args map { arg =>
          arg -> fiddlyCalc(arg, basic, unreachables)
        } checkUniqueSolution {
          case (_, solution) => solution.isDefined
        } flatMap {
          _._2
        }
    }
  }

  private def fiddlyCalc(
    arg: MultiaryForm,
    target: BasicForm,
    unreachables: Seq[MultiaryForm]
  ): Option[Set[MultiaryForm]] =
    arg match {
      case `target` =>
          Some(Set(arg))

      case CompoundMultiaryForm(innerArgs, innerTgt) if innerTgt == target =>
        val allArgsUsed: mutable.Set[MultiaryForm] = new mutable.HashSet[MultiaryForm]()
        if (
          innerArgs.forall { innerArg =>
            //                println(s"potential infinite descent: args/tgt/basic/innerArgs/innerTgt = $args/$tgt/$basic/$innerArgs/$innerTgt")
            if (unreachables.contains(target))
              false
            else {
              canUniquelySolve(args, innerArg, unreachables :+ target) match {
                case Some(argsUsed) =>
                  allArgsUsed ++= argsUsed
                  true
                case None =>
                  false
              }
            }
          }
        )
          Some(allArgsUsed.toSet)
        else
          None
      case _ => None
    }

  /**
    * Tell if a form has a unique solution.
    * @return None if not. Else Some() of the internal args used.
    */
  def solve() =
    canUniquelySolve(args, tgt, Seq())
}

