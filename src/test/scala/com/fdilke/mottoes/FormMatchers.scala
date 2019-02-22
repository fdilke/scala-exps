package com.fdilke.mottoes

import org.scalatest.matchers.{BeMatcher, MatchResult}
import scala.language.reflectiveCalls

object FormMatchers {
  def canonical[
    FORM <: {
      def isCanonical: Boolean
    }
  ]: BeMatcher[FORM] =
    form => MatchResult(
      form.isCanonical,
      s"$form is not canonical",
      s"$form is canonical"
    )

  def uniquelySolvable[
    FORM <: {
      def isUniquelySolvable: Boolean
    }
  ]: BeMatcher[FORM] =
    form => MatchResult(
      form.isUniquelySolvable,
      s"$form is not uniquely solvable",
      s"$form is uniquely solvable"
    )
}
