package com.fdilke.bewl.fsets

import com.fdilke.bewl._

class FiniteSetsDot(val set: Set[Any]) extends ToposDot[FiniteSetsDot, FiniteSetsArrow] {
  override def identity: FiniteSetsArrow = new FiniteSetsArrow(
    this, this,
    Map(set.toList.map(x => (x,x)):_*)
  )

  def x(that: FiniteSetsDot) =
    new BiproductDiagram[FiniteSetsDot, FiniteSetsArrow] {
      override val product = new FiniteSetsDot(for(x <- set; y <- that.set) yield(x, y))

      override val leftProjection = new FiniteSetsArrow(product, FiniteSetsDot.this,
        (for ((x,y)<-product.set) yield ((x, y), x)) toMap
      )
      override val rightProjection = new FiniteSetsArrow(product, that,
        (for ((x,y)<-product.set) yield ((x, y), y)) toMap
      )
      override def multiply(leftArrow: FiniteSetsArrow, rightArrow: FiniteSetsArrow): FiniteSetsArrow =
        new FiniteSetsArrow(leftArrow.source, product,
          (for (x<-leftArrow.source.set) yield (x, (leftArrow.map(x), rightArrow.map(x)))) toMap
      )
    }

  // TODO: add a helper so that we don't need the toMap here (or the brackets)

  override def equals(other: Any): Boolean = other match {
    case that: FiniteSetsDot =>  set == that.set
    case _ => false
  }

  override def hashCode(): Int = set.hashCode()
}

object FiniteSetsDot {
  def from[T](elements: T*) = new FiniteSetsDot(elements.toSet)
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

  override def toString() = s"FiniteSetsArrow(${source.set}, ${target.set}, $map)"

  override def apply(arrow: FiniteSetsArrow): FiniteSetsArrow =
    if (arrow.target == source) {
      new FiniteSetsArrow(arrow.source, target,
        Map(arrow.source.set.toList.map(x => (x, map(arrow.map(x)))):_*)
      )
    } else {
      throw new IllegalArgumentException("Target does not match source")
    }

  override def equals(other: Any): Boolean = other match {
    case that: FiniteSetsArrow =>
      source == that.source && target == that.target && map == that.map
    case _ => false
  }

  override def hashCode(): Int = source.hashCode() + target.hashCode()*5 + map.hashCode()*13
}

object FiniteSetsArrow {
  def from(source: FiniteSetsDot, target: FiniteSetsDot, elements: (Any, Any)*) =
    new FiniteSetsArrow(source, target, Map(elements:_*))
}

object FiniteSets extends Topos[FiniteSetsDot, FiniteSetsArrow] {
  override def biproduct(left: FiniteSetsDot, right: FiniteSetsDot) = left x right
}
