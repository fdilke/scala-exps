package com.fdilke.bewl.fsets

import com.fdilke.bewl.{ToposFixtures, GenericToposTests}

object FiniteSetsFixtures extends ToposFixtures[FiniteSetsDot, FiniteSetsArrow] {
  override val foo: FiniteSetsDot = FiniteSetsDot("a", "b")
  override val bar: FiniteSetsDot = FiniteSetsDot("X", "Y")
  override val foo2bar: FiniteSetsArrow = FiniteSetsArrow(foo, bar, "a"->"X", "b"->"Y")
  override val baz: FiniteSetsDot = FiniteSetsDot(1, 2, 3)
  override val foo2baz: FiniteSetsArrow = FiniteSetsArrow(foo, baz, "a"->1, "b"->3)
}

class FiniteSetsTest extends GenericToposTests(FiniteSets, FiniteSetsFixtures)
