package com.fdilke.bewl.fsets

import com.fdilke.bewl._
import com.fdilke.bewl.fsets.FiniteSets.FiniteSetsUtilities.allMaps
import Function.{tupled, untupled}

object FiniteSets extends Topos {
  type DOT[X] = FiniteSetsDot[X]
  type ARROW[S, T] = FiniteSetsArrow[S, T]
  type BIPRODUCT[L, R] = FiniteSetsBiproduct[L, R]
  type EXPONENTIAL[S, T] = FiniteSetsExponential[S, T]

  val I = FiniteSetsDot[Unit](())

  class FiniteSetsDot[X](val set: Set[X]) extends Dot[X] {
    override def identity: FiniteSetsArrow[X, X] = FiniteSetsArrow(this, this, x => x)

    override def multiply[Y](that: FiniteSetsDot[Y]) = new FiniteSetsBiproduct[X, Y](this, that)

    override def exponential[Y](that: FiniteSetsDot[Y]) = new FiniteSetsExponential[Y, X](that, this)

    override def equals(other: Any): Boolean = other match {
      case that: FiniteSetsDot[X] =>  set == that.set
      case _ => false
    }

    override def hashCode(): Int = set.hashCode()

    override def toConstant = FiniteSetsArrow(this, FiniteSets.I, _ => ())
  }

  case class FiniteSetsArrow[X, Y](val source: FiniteSetsDot[X],
                              val target: FiniteSetsDot[Y],
                              val function: X => Y
                               ) extends Arrow[X, Y] {
    override def toString = s"""FiniteSetsArrow(${source.set}, ${target.set},
      |${Map(source.set.toList.map(x => (x, function(x))): _*)})""".stripMargin

    override def apply[W](arrow: FiniteSetsArrow[W, X]) =
      if (arrow.target == source) {
        FiniteSetsArrow(arrow.source, target,  function.compose(arrow.function))
      } else {
        throw new IllegalArgumentException("Target does not match source")
      }

    override def equals(other: Any): Boolean = other match {
      case that: FiniteSetsArrow[X, Y] =>
        source == that.source && target == that.target &&
          source.set.forall(x => function(x) == that.function(x))
      case _ => false
    }

    override def hashCode(): Int = source.hashCode() + target.hashCode() * 5 + function.hashCode() * 13

    def sanityTest =
      if (!source.set.map(function).forall(target.set.contains)) {
        throw new IllegalArgumentException("Map values not in target")
      }
  }

  class FiniteSetsBiproduct[L, R](left: FiniteSetsDot[L], right: FiniteSetsDot[R]
                                   ) extends Biproduct[L, R] {
    override val product = new FiniteSetsDot[(L, R)](for (x <- left.set; y <- right.set) yield (x, y))

    override val leftProjection = new FiniteSetsArrow[(L, R), L](product, left, tupled { (x, y)  => x} )

    override val rightProjection = new FiniteSetsArrow[(L, R), R](product, right, tupled { (x, y) => y})

    override def multiply[W](leftArrow: FiniteSetsArrow[W, L], rightArrow: FiniteSetsArrow[W, R]) =
      FiniteSetsArrow(leftArrow.source, product, { x => (leftArrow.function(x), rightArrow.function(x))} )
  }

  class FiniteSetsExponential[S, T](source: FiniteSetsDot[S], target: FiniteSetsDot[T])
    extends Exponential[S, T] {

    // the 'function equality' semantics are needed even if these are all maps, because
    // we'll be comparing against things that aren't
    // TODO: can they not be all maps?
    val theAllMaps: Set[S => T] = allMaps(source.set toSeq, target.set).
      map(FunctionWithEquality(source.set, _)).toSet
    val exponentDot: FiniteSetsDot[S => T] = new FiniteSetsDot[S => T](theAllMaps)

    override val evaluation = new BiArrow[S => T, S, T](exponentDot, source,
      FiniteSetsArrow[(S => T, S), T](exponentDot x source, target, tupled {
        (f:S => T, s:S) => f(s)
      }))

    override def transpose[W](multiArrow: BiArrow[W, S, T]) =
      new FiniteSetsArrow[W, S => T](multiArrow.left, exponentDot,
        t => u => multiArrow.arrow.function((t, u))
// TODO: ?        FunctionWithEquality[W, S=>T](multiArrow.left.set, t => u => multiArrow.arrow.function((t, u)))
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
    def cartesian[A](factors: Seq[A]*): Iterator[Seq[A]] = factors match {
      case Seq() => Iterator(Seq())
      case Seq(head, tail @ _*) =>
        head.iterator.flatMap { i => cartesian(tail:_*).map(i +: _) }
    }

    // TODO: change FiniteSet to use traversables
    // TODO: have this iterate over functions
    def allMaps[A, B](source: Seq[A], target: Set[B]): Iterator[Map[A, B]] = source match {
      case Seq() => Iterator(Map[A, B]())
      case Seq(head, tail @ _*) =>
        for (map <- allMaps(tail, target); choice <- target)
        yield map + (head -> choice)
    }
  }
}
