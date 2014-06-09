package com.fdilke.bewl.fsets

import com.fdilke.bewl._
import com.fdilke.bewl.fsets.FiniteSets.FiniteSetsUtilities.allMaps
import Function.tupled

object FiniteSets extends Topos {
  type DOT[X] = FiniteSetsDot[X]
  type ARROW[S, T] = FiniteSetsArrow[S, T]
  type BIPRODUCT[L, R] = FiniteSetsBiproduct[L, R]
  type EXPONENTIAL[S, T] = FiniteSetsExponential[S, T]

  val I = FiniteSetsDot[Unit](())

  class FiniteSetsDot[X](elements: Traversable[X]) extends Dot[X] with Traversable[X] {
    override def toString = elements.toString

    override def foreach[U](f: (X) => U) { elements.foreach(f) }

    override def identity: FiniteSetsArrow[X, X] = FiniteSetsArrow(this, this, x => x)

    override def multiply[Y](that: FiniteSetsDot[Y]) = new FiniteSetsBiproduct[X, Y](this, that)

    override def exponential[Y](that: FiniteSetsDot[Y]) = new FiniteSetsExponential[Y, X](that, this)

    override def toConstant = FiniteSetsArrow(this, FiniteSets.I, _ => ())
  }

  case class FiniteSetsArrow[X, Y](source: FiniteSetsDot[X],
                              target: FiniteSetsDot[Y],
                              function: X => Y
                               ) extends Arrow[X, Y] {
    override def toString = s"""FiniteSetsArrow(${source}, ${target},
      |${Map(source.map(x => (x, function(x))).toList: _*)})""".stripMargin

    override def apply[W](arrow: FiniteSetsArrow[W, X]) =
      if (arrow.target == source) {
        FiniteSetsArrow(arrow.source, target,  function.compose(arrow.function))
      } else {
        throw new IllegalArgumentException("Target does not match source")
      }

    override def equals(other: Any): Boolean = other match {
      case that: FiniteSetsArrow[X, Y] =>
        source == that.source && target == that.target &&
          source.forall(x => function(x) == that.function(x))
      case _ => false
    }

    override def hashCode(): Int = 0 // don't use these as keys

    def sanityTest =
      if (source.map(function).exists(x => target.forall(_ != x))) {
        throw new IllegalArgumentException("Map values not in target")
      }
  }

  class FiniteSetsBiproduct[L, R](left: FiniteSetsDot[L], right: FiniteSetsDot[R]
                                   ) extends Biproduct[L, R] {
    override val product = new FiniteSetsDot[(L, R)](for (x <- left; y <- right) yield (x, y))

    override val leftProjection = new FiniteSetsArrow[(L, R), L](product, left, tupled { (x, y)  => x} )

    override val rightProjection = new FiniteSetsArrow[(L, R), R](product, right, tupled { (x, y) => y})

    override def multiply[W](leftArrow: FiniteSetsArrow[W, L], rightArrow: FiniteSetsArrow[W, R]) =
      FiniteSetsArrow(leftArrow.source, product, { x => (leftArrow.function(x), rightArrow.function(x))} )
  }

  class FiniteSetsExponential[S, T](source: FiniteSetsDot[S], target: FiniteSetsDot[T])
    extends Exponential[S, T] {

    // the 'function equality' semantics are needed even if these are all maps, because
    // we'll be comparing against things that aren't
    val theAllMaps: Traversable[S => T] = allMaps(source, target).
      map(FunctionWithEquality(source, _))
    val exponentDot: FiniteSetsDot[S => T] = new FiniteSetsDot[S => T](theAllMaps)

    override val evaluation = new BiArrow[S => T, S, T](exponentDot, source,
      FiniteSetsArrow[(S => T, S), T](exponentDot x source, target, tupled {
        (f:S => T, s:S) => f(s)
      }))

    override def transpose[W](multiArrow: BiArrow[W, S, T]) =
      new FiniteSetsArrow[W, S => T](multiArrow.left, exponentDot,
        w => FunctionWithEquality[S, T](multiArrow.right, { s => multiArrow.arrow.function((w, s)) })
      )
  }

  object FiniteSetsDot {
    def apply[T](elements: T*) = new FiniteSetsDot(elements.toSet)
  }

  object FiniteSetsBiArrow {
    def apply[L, R, T](left: FiniteSetsDot[L],
                       right: FiniteSetsDot[R],
                       target: FiniteSetsDot[T],
              map: ((L, R), T)*) : BiArrow[L, R, T] =
      BiArrow[L, R, T](left, right,
        FiniteSetsArrow[(L, R), T](left x right, target, Map(map:_*)))
  }

  object FiniteSetsArrow {
    def apply[S, T](source: FiniteSetsDot[S], target: FiniteSetsDot[T], map: (S, T)*): FiniteSetsArrow[S, T] =
      FiniteSetsArrow(source, target, Map(map:_*))
  }

  object FiniteSetsUtilities {
    def cartesian[A](factors: Seq[A]*): Traversable[Seq[A]] = {
      println("factors size: " + factors.size)
      println("factors are: " + factors)
      factors match {
        case Seq() =>
          println("empty!")
          Traversable(Seq())
        case Seq(head, tail@_*) =>
          println("dissected head, tail = " + head + ";;" + tail)
          val ccc = cartesian(tail.toSeq: _*)
          println("cartesian(tail) = " + ccc)
          println("---")
          new Traversable[Seq[A]] {
          override def foreach[U](func: Seq[A] => U) {
            println("tail has size " + tail.size)
            for (h <- head; sequence <- ccc) {
              println("h = " + h)
              func(h +: sequence)
            }
          }
        }
      }
    }

    def allMaps[A, B](source: Traversable[A], target: Traversable[B]): Traversable[A=>B] =
      new Traversable[A=>B] {
        override def foreach[U](func: (A => B) => U): Unit =
          if (source.isEmpty)
            func(_ => ???)
          else
            for(f <- allMaps(source.tail, target);
                choice <- target) {
                func { x => if (x == source.head) choice else f(x) }
              }
    }
  }
}
