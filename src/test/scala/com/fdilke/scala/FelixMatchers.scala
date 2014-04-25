package com.fdilke.scala

import org.scalatest.matchers.{MatchResult, BeMatcher, BePropertyMatcher, BePropertyMatchResult}

object FelixMatchers {
  def anInstanceOf[T](implicit manifest: Manifest[T]) = {
    val clazz = manifest.erasure.asInstanceOf[Class[T]]
    new BeMatcher[AnyRef] {
      def apply(left: AnyRef) =
        MatchResult(clazz.isAssignableFrom(left.getClass),
          "an instance of " + clazz.getName,
          "not an instance of " + clazz.getName)
    }
  }

  def distinct =
    new BeMatcher[AnyRef] {
      def apply(left: AnyRef) = left match {
        case t: Seq[Any] => ???
        case _ => ???
      }
    }
}

