package com.fdilke.mottoes

import com.fdilke.mottoes.UniqueSolution._

import scala.collection.mutable

object FormSolver {
  def apply(form: CompoundMultiaryForm): Option[Set[MultiaryForm]] =
    new FormSolver(
      form.args,
      form.result
    ).solve()
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
        val annotatedArgs: Seq[(MultiaryForm, Option[Set[MultiaryForm]])] =
          args map {
            case basicArg: BasicForm =>
              if (basicArg == basic)
                basicArg -> Some(Set(basicArg : MultiaryForm))
              else
                basicArg -> None

            case compoundArg @ CompoundMultiaryForm(innerArgs, innerTgt) if innerTgt == basic =>
              val allArgsUsed: mutable.Set[MultiaryForm] = new mutable.HashSet[MultiaryForm]()
              if (
                innerArgs.forall { innerArg =>
                //                println(s"potential infinite descent: args/tgt/basic/innerArgs/innerTgt = $args/$tgt/$basic/$innerArgs/$innerTgt")
                if (unreachables.contains(basic))
                  false
                else {
                  canUniquelySolve(args, innerArg, unreachables :+ basic) match {
                    case Some(argsUsed) =>
                      allArgsUsed ++= argsUsed
                      true
                    case None =>
                      false
                  }
                }
              }
            )
              compoundArg -> Some(allArgsUsed.toSet)
            else
              compoundArg -> None
            case otherArg =>
              otherArg -> None
          }

        annotatedArgs checkUniqueSolution {
          case (_, optionalArgsUsed) => optionalArgsUsed.isDefined
        } match {
          case Some((arg, optionalArgsUsed)) => optionalArgsUsed
          case None => None
        }
    }
  }


  def solve() =
    canUniquelySolve(args, tgt, Seq())
}

