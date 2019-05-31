package com.fdilke.wordgraph

import com.fdilke.wordgraph.WordGraphCriterion.adjacent

case class Position(
  words: Set[String],
  prevWord: Option[String]
) {
  def allowedMoves: Set[String] =
    prevWord match {
      case None => words
      case Some(prev) =>
        words.filter {
          adjacent(_, prev)
        }
    }

  def move(newWord: String): Position =
    if (!words.contains(newWord))
      throw new IllegalArgumentException("word not in set")
    else if(
      !prevWord.forall {
        adjacent(_, newWord)
      }
    ) throw new IllegalArgumentException("word not adjacent to previous")
    else
        Position(
          words.filterNot { _ == newWord },
          Some(newWord)
        )

  def winningMoves: Set[String] =
    allowedMoves.filter { newWord =>
      move(newWord).isP
    }

  def isP: Boolean =
    winningMoves.isEmpty

  def isN: Boolean =
    !isP
}
