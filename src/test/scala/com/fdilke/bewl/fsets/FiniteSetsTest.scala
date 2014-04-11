package com.fdilke.bewl.fsets

import com.fdilke.bewl.{ToposFixtures, GenericToposTests}

object FiniteSetsFixtures extends ToposFixtures[FiniteSetsDot, FiniteSetsArrow] {
  override val foo: FiniteSetsDot = FiniteSetsDot.from("a", "b")
  override val bar: FiniteSetsDot = FiniteSetsDot.from("X", "Y")
  override val foo2bar: FiniteSetsArrow = FiniteSetsArrow.from(foo, bar, "a"->"X", "b"->"Y")
}

class FiniteSetsTest extends GenericToposTests(FiniteSets, FiniteSetsFixtures) {

}
