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

  class FiniteSetsDot[X](val set: Set[X]) extends Dot[X] {
    override def identity: FiniteSetsArrow[X, X] = new FiniteSetsArrow(
      this, this,
      Map(set.toList.map(x => (x,x)):_*)
    )

    override def multiply[Y](that: FiniteSetsDot[Y]) = new FiniteSetsBiproduct[X, Y](this, that)

    override def exponential[Y](that: FiniteSetsDot[Y]) = new FiniteSetsExponential[Y, X](that, this)

    override def equals(other: Any): Boolean = other match {
      case that: FiniteSetsDot[X] =>  set == that.set
      case _ => false
    }

    override def hashCode(): Int = set.hashCode()

    override def toConstant = new FiniteSetsArrow(this, FiniteSets.I, _ => ())
  }

  class FiniteSetsArrow[X, Y](val source: FiniteSetsDot[X],
                              val target: FiniteSetsDot[Y],
                              val function: X => Y
                               ) extends Arrow[X, Y] {

    def sanityTest() =
      if (!source.set.map(function).forall(target.set.contains)) {
        throw new IllegalArgumentException("Map values not in target")
      }

    override def toString = s"""FiniteSetsArrow(${source.set}, ${target.set},
      |${Map(source.set.toList.map(x => (x, function(x))): _*)})""".stripMargin

    override def apply[W](arrow: FiniteSetsArrow[W, X]) =
      if (arrow.target == source) {
        new FiniteSetsArrow(arrow.source, target,  function.compose(arrow.function))
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
  }

  class FiniteSetsBiproduct[L, R](left: FiniteSetsDot[L], right: FiniteSetsDot[R]
                                   ) extends Biproduct[L, R] {
    override val product = new FiniteSetsDot[(L, R)](for (x <- left.set; y <- right.set) yield (x, y))

    override val leftProjection: FiniteSetsArrow[(L, R), L] = new FiniteSetsArrow(product, left, tupled { (x, y)  => x} )

    override val rightProjection: FiniteSetsArrow[(L, R), R] = new FiniteSetsArrow(product, right, tupled { (x, y) => y})

    override def multiply[W](leftArrow: FiniteSetsArrow[W, L], rightArrow: FiniteSetsArrow[W, R]) =
      new FiniteSetsArrow(leftArrow.source, product, { case x => (leftArrow.function(x), rightArrow.function(x))} )
  }

  class FiniteSetsExponential[S, T](source: FiniteSetsDot[S], target: FiniteSetsDot[T])
    extends Exponential[S, T] {

    val theAllMaps: Set[S => T] = allMaps(source.set toSeq, target.set).toSet
    val exponentDot: FiniteSetsDot[S => T] = new FiniteSetsDot[S => T](theAllMaps)

    override val evaluation = new BiArrow[S => T, S, T](exponentDot, source,
      new FiniteSetsArrow[(S => T, S), T](exponentDot x source, target, tupled {
        (f:S => T, s:S) => f(s)
      }))

    override def transpose[W](multiArrow: BiArrow[W, S, T]) =
      new FiniteSetsArrow(multiArrow.left, exponentDot, { t =>
        (for (u <- source.set) yield (u, multiArrow.arrow.function((t, u)))).toMap   // TODO: lose the toMap?
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
