package com.fdilke.bewl.fsets

import com.fdilke.bewl._
import FiniteSetsArrow._
import FiniteSetsUtilities._
import com.fdilke.bewl.MultiArrow

class FiniteSetsDot(val set: Set[Any]) extends ToposDot[FiniteSetsDot, FiniteSetsArrow] {
  override def identity: FiniteSetsArrow = new FiniteSetsArrow(
    this, this,
    Map(set.toList.map(x => (x,x)):_*)
  )

  override def x(that: FiniteSetsDot) = new FiniteSetsBiproductDiagram(this, that)

  override def ^(that: FiniteSetsDot) = new FiniteSetsExponentialDiagram(this, that)

  override def equals(other: Any): Boolean = other match {
    case that: FiniteSetsDot =>  set == that.set
    case _ => false
  }

  override def hashCode(): Int = set.hashCode()

  override def toConstant = fromFunction(this, FiniteSets.I, _ => "*")
}

class FiniteSetsBiproductDiagram(left: FiniteSetsDot, right: FiniteSetsDot
  ) extends BiproductDiagram[FiniteSetsDot, FiniteSetsArrow] {
  override val product = new FiniteSetsDot(for (x <- left.set; y <- right.set) yield (x, y))

  override val leftProjection = FiniteSetsArrow.fromFunction(product, left, { case (x, y) => x} )

  override val rightProjection = fromFunction(product, right, { case (x, y) => y})

  override def multiply(leftArrow: FiniteSetsArrow, rightArrow: FiniteSetsArrow): FiniteSetsArrow =
    fromFunction(leftArrow.source, product, { case x => (leftArrow.map(x), rightArrow.map(x))}
  )
}

class FiniteSetsExponentialDiagram(target: FiniteSetsDot, source: FiniteSetsDot)
  extends ExponentialDiagram[FiniteSetsDot, FiniteSetsArrow] {

  val theAllMaps: Set[Any] = allMaps(source.set toSeq, target.set).toSet
  val exponentDot = new FiniteSetsDot(theAllMaps)
  val productExpDiagram = exponentDot x source

  override val evaluation = new MultiArrow[FiniteSetsDot, FiniteSetsArrow](productExpDiagram,
    fromFunction(productExpDiagram.product, target, {
      case (f, x) =>
        f.asInstanceOf[Map[Any, Any]] (x)
    }))

  override def transpose(multiArrow: MultiArrow[FiniteSetsDot, FiniteSetsArrow]): FiniteSetsArrow =
    fromFunction(multiArrow.product.projections(0).target, exponentDot, { t =>
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

class FiniteSetsArrow(
  val source: FiniteSetsDot,
  val target: FiniteSetsDot,
  val map: Map[Any, Any]
) extends ToposArrow[FiniteSetsDot, FiniteSetsArrow] {

  def sanityTest() =
    if (map.keySet != source.set) {
      throw new IllegalArgumentException("Map keys != source")
    } else if (map.values.exists(!target.set.contains(_))) {
      throw new IllegalArgumentException("Map values not in target")
    }

  override def toString = s"FiniteSetsArrow(${source.set}, ${target.set}, $map)"

  override def apply(arrow: FiniteSetsArrow): FiniteSetsArrow =
    if (arrow.target == source) {
      new FiniteSetsArrow(arrow.source, target,
        Map(arrow.source.set.toList.map(x => (x, map(arrow.map(x)))): _*)
      )
    } else {
      throw new IllegalArgumentException("Target does not match source")
    }

  override def equals(other: Any): Boolean = other match {
    case that: FiniteSetsArrow =>
      source == that.source && target == that.target && map == that.map
    case _ => false
  }

  override def hashCode(): Int = source.hashCode() + target.hashCode() * 5 + map.hashCode() * 13
}

object FiniteSetsArrow {
  def apply(source: FiniteSetsDot, target: FiniteSetsDot, elements: (Any, Any)*) =
    new FiniteSetsArrow(source, target, Map(elements: _*))

  def fromFunction(source: FiniteSetsDot, target: FiniteSetsDot, f: Any => Any) =
    new FiniteSetsArrow(source, target, (for (x <- source.set) yield (x, f(x))).toMap)
}

object FiniteSets extends Topos[FiniteSetsDot, FiniteSetsArrow] {
  val I = FiniteSetsDot("*")
}

object FiniteSetsBiArrow {
  def apply(left: FiniteSetsDot, right: FiniteSetsDot, target: FiniteSetsDot,
            map: (Any, Any)*) = {
    val product = left x right
    MultiArrow[FiniteSetsDot, FiniteSetsArrow](product,
      FiniteSetsArrow(product.product, target, map: _*))
  }
}

