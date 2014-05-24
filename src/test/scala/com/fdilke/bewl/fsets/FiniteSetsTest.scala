package com.fdilke.bewl.fsets

import com.fdilke.bewl.{MultiArrow, ToposFixtures, GenericToposTests}

object FiniteSetsFixtures extends ToposFixtures[FiniteSetsDot, FiniteSetsArrow] {
  override val foo = FiniteSetsDot("a", "b")
  override val bar = FiniteSetsDot("X", "Y")
  override val foo2bar = FiniteSetsArrow(foo, bar, "a"->"X", "b"->"Y")
  override val baz = FiniteSetsDot(1, 2, 3)
  override val foo2baz = FiniteSetsArrow(foo, baz, "a"->1, "b"->3)
  override val foobar2baz: MultiArrow[FiniteSetsDot, FiniteSetsArrow] = null
}

class FiniteSetsTest extends GenericToposTests(FiniteSets, FiniteSetsFixtures)
