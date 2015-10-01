package com.fdilke.scala

import org.scalatest.FunSpec

class DependentTypesTests extends FunSpec {
  describe("Dependent types") {
    it("can be used as type parameters in function return values") {
      trait Widget {
        type doodad <: Any
      }
      def fn(widget: Widget) : List[widget.doodad] =
        List[widget.doodad]()
    }

    it("don't have to be types; they can be traits") {
      trait Widget {
        trait Doodad
      }
      def fn(widget: Widget) : List[widget.Doodad] =
        List[widget.Doodad]()
    }

    it("... or even classes") {
      trait Widget {
        class Doodad
      }
      def fn(widget: Widget) : List[widget.Doodad] =
        List[widget.Doodad]()
    }

    it("can be exposed as type parameters in members of an enclosing class") {
      trait Widget {
        type doodad
      }
      class Enclosing(val widget: Widget) {
        def gizmo = new Traversable[widget.doodad] {
          override def foreach[U](f: widget.doodad => U): Unit = ???
        }
      }
    }

    it("can be exposed as higher-kinded type parameters in members of an enclosing class") {
      trait HigherKinded[T]
      trait RequiresHigherKinded[T, HK <: HigherKinded[T]]
      trait Widget {
        type doodad
      }
      class Enclosing(val widget: Widget) {
        trait MyHK extends HigherKinded[widget.doodad]
        def gizmo = new RequiresHigherKinded[widget.doodad, MyHK] {}
      }
    }
  }
}
