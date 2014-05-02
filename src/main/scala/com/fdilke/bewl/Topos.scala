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

trait BiproductDiagram[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] {
  val product: DOT
  val leftProjection: ARROW
  val rightProjection: ARROW
  def multiply(leftArrow: ARROW, rightArrow: ARROW): ARROW
}

trait Topos[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] {
  val I: DOT
}

