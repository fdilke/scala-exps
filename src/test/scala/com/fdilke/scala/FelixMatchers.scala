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

// TODO: investigate matcher mechanism with ClassTag like this:
//def readValue[T:ClassTag](s:String):T = {
//  val tClass = implicitly[ClassTag[T]].runtimeClass
//  //implementation for different classes.
//}


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

