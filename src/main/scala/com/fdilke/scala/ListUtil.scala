package com.fdilke.scala

object ListUtil {
  def count[T](list : List[T], predicate : T => Boolean) = {
    var numFound: Int = 0
    list foreach { t : T =>
      if (predicate(t)) {
        numFound += 1
    }}
    numFound
  }
}
