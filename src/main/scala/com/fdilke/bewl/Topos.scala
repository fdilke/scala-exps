package com.fdilke.bewl

//trait ProductDiagram[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] { self =>
//  val product: DOT
//  val projections: Seq[ARROW]
//  def multiply(arrows: ARROW*): ARROW
//
//  def x(d: DOT) = new ProductDiagram[DOT, ARROW] {
//    private val pXd = self.product x d
//    override val product = pXd.product
//
//    override val projections: Seq[ARROW] = self.projections.map(
//      _(pXd.leftProjection)
//    ) :+ pXd.rightProjection
//
//    override def multiply(arrows: ARROW*): ARROW =
//      if (arrows.size == projections.size)
//        arrows match {
//          case head :+ tail => pXd.multiply(
//              self.multiply(head : _*),
//              tail)
//        }
//      else
//        throw new IllegalArgumentException(s"{projections.size} arrows required")
//    }
//}

trait Topos {
  type DOT[P] <: Dot[P]
  type ARROW[P, Q] <: Arrow[P, Q]
  type BIPRODUCT[P, Q] <: Biproduct[P, Q]
  type EXPONENTIAL[P, Q] <: Exponential[P, Q]

  val I: DOT[Unit]

  trait Dot[X] {
    def identity: ARROW[X, X]

    def toConstant: ARROW[X, Unit]

    def multiply[Y](that: DOT[Y]): BIPRODUCT[X, Y]

    def ^[W](that: DOT[W]): EXPONENTIAL[W, X]

    final def *[Y](that: DOT[Y]) = standardProduct(this.asInstanceOf[DOT[X]], that)

    final def x[Y](that: DOT[Y]) = (this * that).product
  }

  trait Arrow[X, Y] {
    val source: DOT[X]
    val target: DOT[Y]

    def apply[W](arrow: ARROW[W, X]): ARROW[W, Y]

    final def x[Z](that: ARROW[X, Z]) = (this.target * that.target).
      multiply(this.asInstanceOf[ARROW[X, Y]], that)
  }

  trait Biproduct[X, Y] {
    val product: DOT[(X, Y)]
    val leftProjection: ARROW[(X, Y), X]
    val rightProjection: ARROW[(X, Y), Y]

    def multiply[W](leftArrow: ARROW[W, X],
                    rightArrow: ARROW[W, Y]): ARROW[W, (X, Y)]
  }

// Fux this! Use old ProductDiagram above as inspiration
//  trait Product[X, Y] {
//    val product: DOT[(X, Y)]
//    def projections: ARROW[(X, Y), X]
//
//    def multiply[W](leftArrow: ARROW[W, X],
//                    rightArrow: ARROW[W, Y]): ARROW[W, (X, Y)]
//  }

  trait Exponential[S, T] {
    val evaluation: BiArrow[S => T, S, T]

    def transpose[W](multiArrow: BiArrow[W, S, T]): ARROW[W, S => T]
  }

  case class BiArrow[L, R, T](left: DOT[L], right: DOT[R], arrow: ARROW[(L, R), T])

  private val standardProducts = scala.collection.mutable.Map[(DOT[Any], DOT[Any]), BIPRODUCT[Any, Any]]()
  def standardProduct[X, Y](x: DOT[X], y: DOT[Y]): BIPRODUCT[X, Y] = {
    val key: (DOT[Any], DOT[Any]) = (x.asInstanceOf[DOT[Any]], y.asInstanceOf[DOT[Any]])
    standardProducts.get(key).map( product =>
      product.asInstanceOf[BIPRODUCT[X, Y]]
    ).getOrElse {
      val product: BIPRODUCT[X, Y] = x multiply y
      standardProducts.put(key, product.asInstanceOf[BIPRODUCT[Any, Any]])
      product
    }
  }

  def leftProjection[X, Y](x: DOT[X], y: DOT[Y]) = (x * y).leftProjection
  def rightProjection[X, Y](x: DOT[X], y: DOT[Y]) = (x * y).rightProjection

  // Helper methods for triproducts (this could obviously be extended)
  def leftProjection[X, Y, Z](x: DOT[X], y: DOT[Y], z: DOT[Z]) = (x * y).leftProjection(((x x y) * z).leftProjection)
  def midProjection[X, Y, Z](x: DOT[X], y: DOT[Y], z: DOT[Z]) = (x * y).rightProjection(((x x y) * z).leftProjection)
  def rightProjection[X, Y, Z](x: DOT[X], y: DOT[Y], z: DOT[Z]) = ((x x y) * z).rightProjection
}

