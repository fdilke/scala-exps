package com.fdilke.wordgraph

case class Position(
  words: Set[String],
  prevWord: Option[String]
) {
  def isP(): Boolean =
    words.isEmpty
}
