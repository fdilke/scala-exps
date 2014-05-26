package com.fdilke.bewl.fsets

import com.fdilke.bewl._
import FiniteSetsArrow._
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

  // TODO: delete? move?
  def cartesian[A](factors: Seq[Seq[A]]): Iterator[Seq[A]] = factors match {
    case Seq() => Iterator(Seq())
    case Seq(head, tail @ _*) =>
      head.iterator.flatMap { i => cartesian(tail).map(i +: _) }
  }

  // TODO: find a better place to put this. Test it properly
  // TODO: change FiniteSet to use iterators
  def allMaps[A, B](source: Seq[A], target: Set[B]): Iterator[Map[A, B]] = source match {
    case Seq() => Iterator(Map[A, B]())
    case Seq(head, tail @ _*) =>
      for (map <- allMaps(tail, target); choice <- target)
        yield map + (head -> choice)
  }

  val exponentDot = FiniteSetsDot(allMaps(source.set toSeq, target.set) toSet)
  val productExpDiagram = new FiniteSetsBiproductDiagram(exponentDot, source)
  val productDot = productExpDiagram.product

  val eval = fromFunction(productDot, target, {
    case (f: Map[Any, Any], x) => f(x)
  })

  override val evaluation = new MultiArrow[FiniteSetsDot, FiniteSetsArrow](productExpDiagram, eval)

  override def transpose(multiArrow: MultiArrow[FiniteSetsDot, FiniteSetsArrow]): FiniteSetsArrow = null
//  public FiniteSetArrow getTranspose(MultiArrow<FiniteSet, FiniteSetArrow> f2) {
//    FiniteSet originalSource = _evaluation.getProductDiagram().getProjections().get(1).getTarget();
//    FiniteSet originalTarget = _evaluation.getArrow().getTarget();
//    FiniteSet exponentialObject = _evaluation.getProductDiagram().getProjections().get(0).getTarget();
//    // should these be members? It's relatively quick to extract them.
//
//    // make sure it's a multi-arrow of the right type, something x source -> target
//    ProductDiagram<FiniteSet, FiniteSetArrow> productDiagram = f2.getProductDiagram();
//    if (productDiagram.getProjections().size() != 2) {
//      throw new IllegalArgumentException("multi-arrow has wrong arity");
//    } else if (productDiagram.getProjections().get(1).getTarget() != originalSource) {
//      throw new IllegalArgumentException("multi-arrow has wrong type in 2nd argument");
//    } else if (f2.getArrow().getTarget() != originalTarget) {
//      throw new IllegalArgumentException("multi-arrow has wrong return type");
//    }
//    FiniteSet transposeSource = productDiagram.getProjections().get(0).getTarget();
//    Map<Object, Object> map = new HashMap<Object, Object>();
//    for (Object transposeSourceObject : transposeSource.getUnderlyingSet()) {
//      Object[] functionArray = new Object[originalSource.getUnderlyingSet().size()];
//      for (Object originalSourceObject : originalSource.getUnderlyingSet()) {
//        Object[] pair = new Object[]{transposeSourceObject, originalSourceObject};
//        Object equivalentPair = FiniteSetsMultiplier.findEquivalentArray(f2.getArrow().getSource(), pair);
//        Object image = f2.getArrow().getUnderlyingMap().get(equivalentPair);
//        int index = _indexLookup.get(originalSourceObject);
//        functionArray[index] = image;
//      }
//      map.put(transposeSourceObject, FiniteSetsMultiplier.findEquivalentArray(exponentialObject, functionArray));
//    }
//    return new FiniteSetArrow(transposeSource, exponentialObject, map);
//  }
}

object FiniteSetsDot {
  def apply[T](elements: T*) = new FiniteSetsDot(elements.toSet)
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


