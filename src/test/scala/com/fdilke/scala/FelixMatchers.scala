package com.fdilke.scala

import org.scalatest.matchers.{MatchResult, BeMatcher, BePropertyMatcher, BePropertyMatchResult}

import scala.reflect.ClassTag

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

  def ofType[T:ClassTag] = {
    val tClass = implicitly[ClassTag[T]].runtimeClass
    new BeMatcher[AnyRef] {
      def apply(left: AnyRef) =
        MatchResult(tClass.isAssignableFrom(left.getClass),
          "an instance of " + tClass.getName,
          "not an instance of " + tClass.getName)
    }
  }

//  def distinct =
//    new BeMatcher[AnyRef] {
//      def apply(left: AnyRef) = left match {
//        case t: Seq[Any] => ???
//        case _ => ???
//      }
//    }

  // scratch...

//  def unchanged[T](t: T):T = t
//
//  def useIt(function[T]:  T => T) = ???
//
//  useIt(unchanged)

  // add test that uses this update mechanism:
//  char.arrow(monicBar2baz) = truth(bar.toI)

}

