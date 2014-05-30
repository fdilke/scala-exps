package com.fdilke.bewl.fsets

import com.fdilke.bewl.{GenericToposTests, ToposWithFixtures}
import com.fdilke.bewl.fsets.FiniteSets.{FiniteSetsBiArrow, FiniteSetsArrow, FiniteSetsDot}

class FiniteSetsTest extends GenericToposTests(new ToposWithFixtures {
  type TOPOS = FiniteSets.type
  val topos = FiniteSets

  type FOO = Boolean
  type BAR = String
  type BAZ = Int

  override val foo: DOT[FOO] = FiniteSetsDot[FOO](true, false)
  override val bar = FiniteSetsDot[BAR]("X", "Y")
  override val foo2bar = FiniteSetsArrow[FOO, BAR](foo, bar, true -> "X", false -> "Y")
  override val baz = FiniteSetsDot[BAZ](1, 2, 3)
  override val foo2baz = FiniteSetsArrow[FOO, BAZ](foo, baz, true -> 1, false -> 3)

  override val foobar2baz = FiniteSetsBiArrow[FOO, BAR, BAZ](
    foo, bar, baz, (true, "X") -> 2, (false, "X") -> 3, (true, "Y") -> 1, (false, "Y") -> 2
  )
})

