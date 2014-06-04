package com.fdilke.bewl.fsets

import com.fdilke.bewl._
import com.fdilke.bewl.fsets.FiniteSets.FiniteSetsArrow.fromFunction
import com.fdilke.bewl.fsets.FiniteSets.FiniteSetsUtilities.allMaps

object FiniteSets extends Topos {
  type DOT[X] = FiniteSetsDot[X]
  type ARROW[S, T] = FiniteSetsArrow[S, T]
  type BIPRODUCT[L, R] = FiniteSetsBiproduct[L, R]
  type EXPONENTIAL[S, T] = FiniteSetsExponential[S, T]

  val I = FiniteSetsDot[Unit](())

  class FiniteSetsDot[X](val set: Set[X]) extends Dot[X] {
    override def identity: FiniteSetsArrow[X, X] = new FiniteSetsArrow(
      this, this,
      Map(set.toList.map(x => (x,x)):_*)
    )

    override def multiply[Y](that: FiniteSetsDot[Y]) = new FiniteSetsBiproduct[X, Y](this, that)

    override def ^[Y](that: FiniteSetsDot[Y]) = new FiniteSetsExponential[Y, X](that, this)

    override def equals(other: Any): Boolean = other match {
      case that: FiniteSetsDot[X] =>  set == that.set
      case _ => false
    }

    override def hashCode(): Int = set.hashCode()

    override def toConstant = fromFunction(this, FiniteSets.I, _ => ())
  }

  class FiniteSetsArrow[X, Y](val source: FiniteSetsDot[X],
                              val target: FiniteSetsDot[Y],
                              val map: Map[X, Y]
                               ) extends Arrow[X, Y] {

    def sanityTest() =
      if (map.keySet != source.set) {
        throw new IllegalArgumentException("Map keys != source")
      } else if (map.values.exists(!target.set.contains(_))) {
        throw new IllegalArgumentException("Map values not in target")
      }

    override def toString = s"FiniteSetsArrow(${source.set}, ${target.set}, $map)"

    override def apply[W](arrow: FiniteSetsArrow[W, X]) =
      if (arrow.target == source) {
        new FiniteSetsArrow(arrow.source, target,
          Map(arrow.source.set.toList.map(x => (x, map(arrow.map(x)))): _*)
        )
      } else {
        throw new IllegalArgumentException("Target does not match source")
      }

    override def equals(other: Any): Boolean = other match {
      case that: FiniteSetsArrow[X, Y] =>
        source == that.source && target == that.target && map == that.map
      case _ => false
    }

    override def hashCode(): Int = source.hashCode() + target.hashCode() * 5 + map.hashCode() * 13
  }

  class FiniteSetsBiproduct[L, R](left: FiniteSetsDot[L], right: FiniteSetsDot[R]
                                   ) extends Biproduct[L, R] {
    override val product = new FiniteSetsDot[(L, R)](for (x <- left.set; y <- right.set) yield (x, y))

    override val leftProjection: FiniteSetsArrow[(L, R), L] = fromFunction(product, left, { case(x, y)  => x} )

    override val rightProjection: FiniteSetsArrow[(L, R), R] = fromFunction(product, right, { case (x, y) => y})

    override def multiply[W](leftArrow: FiniteSetsArrow[W, L], rightArrow: FiniteSetsArrow[W, R]) =
      fromFunction(leftArrow.source, product, { case x => (leftArrow.map(x), rightArrow.map(x))} )
  }

  class FiniteSetsExponential[S, T](source: FiniteSetsDot[S], target: FiniteSetsDot[T])
    extends Exponential[S, T] {

    val theAllMaps: Set[S => T] = allMaps(source.set toSeq, target.set).toSet
    val exponentDot = new FiniteSetsDot[S => T](theAllMaps)

    // TODO: use FiniteSetsBiArrow
    override val evaluation = new BiArrow[S => T, S, T](exponentDot, source,
      fromFunction(exponentDot x source, target, {
        case (f, s) =>
          f.asInstanceOf[Map[S, T]](s)
      }))

    override def transpose[W](multiArrow: BiArrow[W, S, T]) =
      fromFunction(multiArrow.left, exponentDot, { t =>
        (for (u <- source.set) yield (u, multiArrow.arrow.map((t, u)))).toMap
      })
  }

  object FiniteSetsDot {
    def apply[T](elements: T*) = new FiniteSetsDot(elements.toSet)
  }

  object FiniteSetsUtilities {
    def cartesian[A](factors: Seq[A]*): Iterator[Seq[A]] = factors match {
      case Seq() => Iterator(Seq())
      case Seq(head, tail @ _*) =>
        head.iterator.flatMap { i => cartesian(tail:_*).map(i +: _) }
    }

    // TODO: change FiniteSet to use iterators
    def allMaps[A, B](source: Seq[A], target: Set[B]): Iterator[Map[A, B]] = source match {
      case Seq() => Iterator(Map[A, B]())
      case Seq(head, tail @ _*) =>
        for (map <- allMaps(tail, target); choice <- target)
        yield map + (head -> choice)
    }
  }

  object FiniteSetsArrow {
    def apply[S, T](source: FiniteSetsDot[S], target: FiniteSetsDot[T], elements: (S, T)*) =
      new FiniteSetsArrow[S, T](source, target, Map(elements: _*))

    def fromFunction[S, T](source: FiniteSetsDot[S], target: FiniteSetsDot[T], f: S => T) =
      new FiniteSetsArrow(source, target, (for (x <- source.set) yield (x, f(x))).toMap)
  }

  object FiniteSetsBiArrow {
    def apply[L, R, T](left: FiniteSetsDot[L],
                       right: FiniteSetsDot[R],
                       target: FiniteSetsDot[T],
              map: ((L, R), T)*) : BiArrow[L, R, T] =
      BiArrow[L, R, T](left, right,
        FiniteSetsArrow(left x right, target, map: _*))
  }
}
