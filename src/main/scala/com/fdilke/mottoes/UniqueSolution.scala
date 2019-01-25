package com.fdilke.mottoes

object UniqueSolution {
  implicit class FindUniqyeSolution[A](
    candidates: Seq[A]
  ) {
    def hasUniqueSolution(criterion: PartialFunction[A, Boolean]): Boolean = {
      var numSolutions: Int = 0
      candidates takeWhile { candidate =>
        if (numSolutions <= 1) {
          try {
            if (criterion(candidate))
              numSolutions += 1
            true
          } catch {
            case _: AbandonUniqueSearchException =>
              numSolutions = 2 // finish the search
              false
          }
        } else
          false
      }
      numSolutions == 1
    }
  }

  class AbandonUniqueSearchException extends Exception
}

