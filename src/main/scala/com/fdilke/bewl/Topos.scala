package com.fdilke.bewl

trait ToposDot[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] {
  def identity : ARROW
}

trait ToposArrow[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] {
  def source : DOT
  def target : DOT
  def apply(arrow : ARROW) : ARROW
}

trait Topos[DOT <: ToposDot[DOT, ARROW], ARROW <: ToposArrow[DOT, ARROW]] {

}

