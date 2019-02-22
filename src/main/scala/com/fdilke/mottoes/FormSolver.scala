package com.fdilke.mottoes

import com.fdilke.mottoes.UniqueSolution._

import scala.collection.mutable
import scala.language.postfixOps

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
        if (args.toSet.intersect(innerArgs.toSet).nonEmpty)
          None
        else
          canUniquelySolve(args ++ innerArgs, innerTgt, unreachables) map {
            _ intersect args.toSet
          }
      case basic: BasicForm =>
        args map { arg =>
          arg -> fiddlyCalc(arg, basic, unreachables)
        } checkUniqueSolution {
          _._2.isDefined
        } map {
          case (arg, _) => Set(arg)
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
//          Some(allArgsUsed.toSet)
          Some(Set(arg))
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

