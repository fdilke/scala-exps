package com.fdilke.bewl

trait ToposDot[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] {
  def identity : ARROW
  def toConstant: ARROW
  def x(that: DOT): BiproductDiagram[DOT, ARROW]
}

trait ToposArrow[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] {
  val source : DOT
  val target : DOT
  def apply(arrow : ARROW) : ARROW
}

trait ProductDiagram[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] { self =>
  val product: DOT
  val projections: Seq[ARROW]
  def multiply(arrows: ARROW*): ARROW

  def x(d: DOT) = new ProductDiagram[DOT, ARROW] {
    private val pXd = self.product x d
    override val product = pXd.product

    override val projections: Seq[ARROW] = self.projections.map(
      _(pXd.leftProjection)
    ) :+ pXd.rightProjection

    override def multiply(arrows: ARROW*): ARROW =
      if (arrows.size == projections.size)
        arrows match {
          case head :+ tail =>
            pXd.multiply(
              self.multiply(head : _*),
              tail)
        }
      else
        throw new IllegalArgumentException(s"{projections.size} arrows required")
    }
}

trait BiproductDiagram[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]]
  extends ProductDiagram[DOT, ARROW] {
  val leftProjection: ARROW
  val rightProjection: ARROW
  def multiply(leftArrow: ARROW, rightArrow: ARROW): ARROW

  override val product: DOT
  override lazy val projections: Seq[ARROW] = Seq(leftProjection, rightProjection)
  override def multiply(arrows: ARROW*) = if (arrows.size == 2)
    multiply(arrows(0), arrows(1))
  else
    throw new IllegalArgumentException("biproduct multiplication requires two arrows")
}

trait Topos[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] {
  val I: DOT
}

