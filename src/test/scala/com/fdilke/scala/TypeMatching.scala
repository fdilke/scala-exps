package com.fdilke.scala

import org.scalatest.matchers.BePropertyMatcher
import org.scalatest.matchers.BePropertyMatchResult

object TypeMatching {
  def anInstanceOf[T](implicit manifest: Manifest[T]) = {
    val clazz = manifest.erasure.asInstanceOf[Class[T]]
    new BePropertyMatcher[AnyRef] {
      def apply(left: AnyRef) =
        BePropertyMatchResult(clazz.isAssignableFrom(left.getClass), "an instance of " + clazz.getName)
    }
  }
}
