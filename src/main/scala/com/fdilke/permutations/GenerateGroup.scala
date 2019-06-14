package com.fdilke.permutations

object GenerateGroup {
  def apply(generators: OldPermutation*) = {
    if (generators.isEmpty) {
      new GenerateGroup(Set(OldPermutation.identity))
    } else {
      val g = generators.toSet

      def genloop(t: Set[OldPermutation], x: Set[OldPermutation]): Set[OldPermutation] = {
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

  def multiplySets(a : Set[OldPermutation], b : Set[OldPermutation]) : Set[OldPermutation] =
    for (x <- a; y <- b)
      yield x(y)
}

class GenerateGroup(val set: Set[OldPermutation]) {
}

