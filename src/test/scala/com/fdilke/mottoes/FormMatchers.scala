package com.fdilke.mottoes

import org.scalatest.matchers.{BeMatcher, MatchResult}

object FormMatchers {
  val canonical: BeMatcher[BinaryForm] =
    form => MatchResult(
      form.isCanonical,
      s"$form is not canonical",
      s"$form is canonical"
    )

  val uniquelySolvable: BeMatcher[BinaryForm] =
    form => MatchResult(
      form.isUniquelySolvable,
      s"$form is not uniquely solvable",
      s"$form is uniquely solvable"
    )
}
