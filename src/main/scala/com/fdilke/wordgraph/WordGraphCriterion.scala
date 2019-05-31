package com.fdilke.wordgraph

import scala.collection.JavaConverters._

object WordGraphCriterion {
  def letters(string: String) =
    string.chars().iterator().asScala.toSet

  private def unequalAndWithACommonLetter(str1: String, str2: String): Boolean =
    (str1 != str2) &&
      letters(str1).intersect(
        letters(str2)
      ).exists {
        x => !x.toChar.isWhitespace
      }

  def adjacent(str1: String, str2: String): Boolean =
    unequalAndWithACommonLetter(
      str1.toLowerCase(),
      str2.toLowerCase()
    )
}
