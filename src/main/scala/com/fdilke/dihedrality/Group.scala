package com.fdilke.dihedrality

object Group {
  def generate(generators: Permutation*) = {
    if (generators.isEmpty) {
      throw new UnsupportedOperationException("No generators")
    }

    val g = generators.toSet

    def genloop(t : Set[Permutation], x: Set[Permutation]) : Set[Permutation] = {
      val xg = multiplySets(x, g)
      val xg_t = xg -- t
      if (xg_t.isEmpty) {
        t
      } else {
        genloop(t ++ xg_t, xg_t)
      }
    }

    new Group(genloop(g, g))
  }

  def multiplySets(a : Set[Permutation], b : Set[Permutation]) : Set[Permutation] =
    for (x <- a; y <- b)
      yield x(y)
}

class Group(val set: Set[Permutation]) {
}

