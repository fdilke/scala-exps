package com.fdilke.scala

import scala.language.higherKinds

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

object BootstrapMethod { // Failed experiment in trying to use a method before the object's been constructed

  trait BootstrapBase[A] {
    def method(): A = ???
  }

  class BootstrapSubclass[A](member: A) extends BootstrapBase[A]

  def bootstrap = new BootstrapSubclass[Any](
    // BootstrapMethod.BootstrapSubclass.this.method(None)
    null.asInstanceOf[Any]
  )
}

object FancyMethodNames extends App {
  def ∀(n: Int) = println("How cool is that")
  ∀(0)

  try {
    1/0
  } catch {
    case π: Exception =>
      π.printStackTrace()
  }
}

object WrappingQuandary {
  trait ToyTopos {
    type ELEMENT
  }

  object FiniteSets extends ToyTopos {
    override type ELEMENT = Int
  }

  class DerivedToyTopos(val topos: ToyTopos) extends ToyTopos {
    override type ELEMENT = topos.ELEMENT
  }

  abstract class ToyToposWithFixtures {
    val topos: ToyTopos
    type FOO <: topos.ELEMENT
  }

  new ToyToposWithFixtures {
//    override val topos = new DerivedToyTopos(FiniteSets)
    override val topos = FiniteSets
    override type FOO = Int
  }
}

object OverridingObsession {
  trait Wrappings {

  }

  trait ToyTopos {
    type ELEMENT
  }

  trait Fixtures {
    val topos: ToyTopos
  }

  val toyTopos = new ToyTopos with Wrappings {
    override type ELEMENT = Int
  }

  new Fixtures {
    val topos : ToyTopos with Wrappings = toyTopos
  }
}

// lazy object LazyObject {}
// no such thing!

object CurriedConstructor {
  class Widget(n: Int, text: String) {
    def this(text: String)(n: Int) = this(n, text)
  }

  class UberWidget extends Widget("thingy")(2)

  class Doodad(func: Int => Int, n: Int) {
    def this(n: Int)(func: Int => Int) = this(func, n)
  }

  class UberDoodad extends Doodad(2)({ (n: Int) => n + 1 })
}

object OverridingAbstractTypes {
  trait WidgetFun {
    type WIDGET = String
    type CONTAINER[X] <: Traversable[X]
  }

  class NewWidgets extends WidgetFun {
// can't! "Incompatible"
//    override type WIDGET = Int
// but can tighten the bounds:
    override type CONTAINER[X] = List[X]
  }
}

trait ExistentialTypes {
  trait ElementProxy[T]
  type STAR[ElementProxy[T]] = Array[String]
}

