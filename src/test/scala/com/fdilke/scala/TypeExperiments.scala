package com.fdilke.scala

// User: Felix Date: 29/05/2014 Time: 18:30

trait Terminal

trait Dot[X] {
  def identity : Arrow[X, X]
  def toConstant: Arrow[X, Terminal]
  def x[Y](that: Dot[Y]): Biproduct[X, Y]
  def ^[Y](that: Dot[Y]): Exponential[Y, X]
}

trait Arrow[X, Y] {
  val source : Dot[X]
  val target : Dot[Y]
  def apply[W](arrow : Arrow[W, X]) : Arrow[W, Y]
}

trait Biproduct[X, Y] {
  val leftProjection: Arrow[(X, Y), X]
  val rightProjection: Arrow[(X, Y), Y]
  def multiply[W](leftArrow: Arrow[W, X], rightArrow: Arrow[W, Y]): Arrow[W, (X, Y)]
}

trait Exponential[S, T] {
  val evaluation: BiArrow[S => T, S, T]
  def transpose[W](multiArrow: BiArrow[W, S, T]): Arrow[W, S => T]
}

case class BiArrow[L, R, T] (product: Biproduct[L, R], arrow: Arrow[(L, R), T])

// MY stuff

// Monad stuff

trait Monad[M[_]] {
  def eta[X]: X => M[X]
  def map[X, Y]: (X => Y) => (M[X] => M[Y])
  def flatten[X]: M[M[X]] => M[X]
}

class MyOption[X](val option: Option[X])

object MyOption extends Monad[MyOption] {
  override def eta[X]: X => MyOption[X] = { x => new MyOption(Some(x)) }

  override def map[X, Y]: (X => Y) => (MyOption[X] => MyOption[Y]) = { f => { optX =>
    new MyOption(optX.option.map(f))
  }}

  override def flatten[X]: MyOption[MyOption[X]] => MyOption[X] = { optOptX =>
    new MyOption[X](optOptX.option.map(optX => optX.option).flatten)
  }
}