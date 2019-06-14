package com.fdilke.permutations

object GenerateGroup {
  def apply(generators: Permutation*) = {
    if (generators.isEmpty) {
      new GenerateGroup(Set(Permutation.identity))
    } else {
      val g = generators.toSet

      def genloop(t: Set[Permutation], x: Set[Permutation]): Set[Permutation] = {
        val xg = multiplySets(x, g)
        val xg_t = xg -- t
        if (xg_t.isEmpty) {
          t
        } else {
          genloop(t ++ xg_t, xg_t)
        }
      }

      new GenerateGroup(genloop(g, g))
    }
  }

  def multiplySets(a : Set[Permutation], b : Set[Permutation]) : Set[Permutation] =
    for (x <- a; y <- b)
      yield x(y)
}

class GenerateGroup(val set: Set[Permutation]) {
}

