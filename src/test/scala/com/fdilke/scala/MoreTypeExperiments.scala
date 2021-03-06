package com.fdilke.scala

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

import scala.language.existentials

object MoreTypeExperiments {

  trait Topos {
    type DOT[P] <: Dot[P]
    type ARROW[P, Q] <: Arrow[P, Q]

    trait Dot[X] {
      def identity: ARROW[X, X]
    }

    trait Arrow[X, Y] {
      def apply[W](arrow: ARROW[W, X]): ARROW[W, Y]
    }
  }

  trait ToposFixtures[T <: Topos] {
    type FOO
    type BAR

    val foo: T#DOT[FOO]
    val bar: T#DOT[BAR]

    val foo2bar: T#ARROW[FOO, BAR]
  }

  abstract class SimplifiedToposTests[
  T <: Topos,
  F <: ToposFixtures[T]
  ](topos: T, fixtures: F) extends AnyFunSpec {
    self =>

    describe(s"The topos ${topos.getClass.getSimpleName}") {
      it("has identity arrows which can be composed") {
        // result is confusion about what all these types are
        //        val f2b: T#ARROW[F#FOO, F#BAR] = fixtures.foo2bar
        //        val f2b: T#ARROW[SimplifiedToposTests.this.type#fixtures#FOO, SimplifiedToposTests.this.type#fixtures#BAR] = fixtures.foo2bar
        //
        //        val i = fixtures.foo.identity
        //        f2b.apply[fixtures.type#FOO](i)
      }
    }
  }
}

object TypeProjectionHell {

  trait ToposLite {
    type ELEMENT
    type *[SS <: ELEMENT] <: (SS, SS) with ELEMENT
  }

  object Ɛ extends ToposLite {
    override type ELEMENT = Any
    override type *[SS <: ELEMENT] = (SS, SS) with ELEMENT

    trait ElementWrapper[A <: ELEMENT, AA <: ElementWrapper[A, AA]] {
      wrapper =>
      final type BASE = A
      val element: A

      def apply[F, G](f: F, g: G): Pair[F, G] with ElementWrapper[A, AA] =
        new Pair[F, G](f, g) with ElementWrapper[A, AA] {
          override val element = wrapper.element
        }

      def apply[F, G](f2g: F => G): ElementWrapper[A, AA] with (F => G) =
        new (F => G) with ElementWrapper[A, AA] {
          def apply(f: F): G = f2g(f)

          override val element = wrapper.element
        }

      def humdrum(aa: AA, starA: Ɛ.*[A]): Pair[AA, AA] with Ɛ.ElementWrapper[Ɛ.*[BASE], H] forSome {
        type H <: Ɛ.ElementWrapper[Ɛ.*[BASE], H]
      } = {
        class RecursiveStar[A <: ELEMENT](starA: Ɛ.*[A]) extends Pair[AA, AA](aa, aa) with ElementWrapper[Ɛ.*[A], RecursiveStar[A]] {
          override val element = starA
        }
        new RecursiveStar[A](starA)
      }
    }

    object ElementWrapper {

      class ELementWrapperRecursive[A <: ELEMENT](a: A) extends
      ElementWrapper[A, ELementWrapperRecursive[A]] {
        override val element = a
      }

      def apply[A <: ELEMENT](a: A) = new ELementWrapperRecursive[A](a)
    }
  }

  object DarkLabyrinth extends ToposLite {

    override type ELEMENT = Ɛ.ElementWrapper[A, AA] forSome {
      type A <: Ɛ.ELEMENT
      type AA <: Ɛ.ElementWrapper[A, AA]
    }

    override type *[SS <: ELEMENT] = (SS, SS) with ELEMENT

    type **[
    S <: Ɛ.ELEMENT,
    SS <: Ɛ.ElementWrapper[S, SS],
    H <: Ɛ.ElementWrapper[Ɛ.*[SS#BASE], H]
    ] = Pair[SS, SS] with Ɛ.ElementWrapper[Ɛ.*[S], H]

    def onAPlate[
    S <: Ɛ.ELEMENT,
    SS <: Ɛ.ElementWrapper[S, SS],
    H <: Ɛ.ElementWrapper[Ɛ.*[S], H]
    ](
       star: Pair[SS, SS] with Ɛ.ElementWrapper[Ɛ.*[S], H]
       ): **[S, SS, H] = star

    def `try*`[
    Z <: Ɛ.ELEMENT, ZZ <: Ɛ.ElementWrapper[Z, ZZ]
    ](zz: ZZ, starZ: Ɛ.*[Z]): **[Z, ZZ, _] = {
      zz.humdrum(zz, starZ)
    }
  }
}
  
object DeeperTypeProjectionHell {

  trait ToposLite { Ɛ =>
    type ELEMENT
    type x[T <: ELEMENT, U <: ELEMENT] <: Pair[T, U] with ELEMENT

    trait VeiledElementWrapper[AA <: VeiledElementWrapper[AA]] {
      type BASE <: ELEMENT
    }

    trait ElementWrapper[A <: ELEMENT, AA <: ElementWrapper[A, AA]] extends
      VeiledElementWrapper[AA] { wrapper =>
      final type BASE = A
      val element: A

      def apply[F, G](f: F, g: G): Pair[F, G] with ElementWrapper[A, AA] =
        new Pair[F, G](f, g) with ElementWrapper[A, AA] {
          override val element = wrapper.element
        }

      def apply[F, G](f2g: F => G): (F => G) with ElementWrapper[A, AA] =
        new (F => G) with ElementWrapper[A, AA] {
          def apply(f: F): G = f2g(f)
          override val element = wrapper.element
        }

      trait MonoidLite[M <: ELEMENT]

      class ActionsLite[M <: ELEMENT](monoid: MonoidLite[M]) extends ToposLite {

        // type ELEMENT = AA forSome {
        //   type AA <: Ɛ.VeiledElementWrapper[AA]
        // }
        type ELEMENT = AA forSome {
          type A <: Ɛ.ELEMENT
          type AA <: Ɛ.ElementWrapper[A, AA]
        }

        trait VeiledProductElementWrapper[
          ZZ <: Ɛ.VeiledElementWrapper[ZZ],
          AA <: Ɛ.VeiledElementWrapper[AA],
          H <: Ɛ.ElementWrapper[Ɛ.x[ZZ#BASE, AA#BASE], H]
        ] extends Ɛ.ElementWrapper[
          Ɛ.x[ZZ#BASE, AA#BASE], H
        ] {
          type PRODUCT = Pair[ZZ, AA] with Ɛ.ElementWrapper[
            Ɛ.x[ZZ#BASE, AA#BASE], H
          ]            
          val finalProduct: PRODUCT
        }

        class ProductElementWrapper[
          Z <: Ɛ.ELEMENT,
          ZZ <: Ɛ.ElementWrapper[Z, ZZ],
          A <: Ɛ.ELEMENT,
          AA <: Ɛ.ElementWrapper[A, AA]
        ] (
          zxa: Ɛ.x[Z, A],
          zz: ZZ,
          aa: AA
        ) extends Pair[ZZ, AA](zz, aa) with Ɛ.ElementWrapper[
          Ɛ.x[Z, A], 
          ProductElementWrapper[Z, ZZ, A, AA]
        ] with VeiledProductElementWrapper[ZZ, AA, ProductElementWrapper[Z, ZZ, A, AA]] {
          override val element = zxa

          override val finalProduct: Pair[ZZ, AA] with Ɛ.ElementWrapper[
            Ɛ.x[Z, A],
            ProductElementWrapper[Z, ZZ, A, AA]
          ] = this
        }

        override type x[T <: ELEMENT, U <: ELEMENT] <: Pair[T, U] with ELEMENT
        
        // override type x[ZZ <: ELEMENT, AA <: ELEMENT] = 
        //   VeiledProductElementWrapper[ZZ, AA, H]#PRODUCT forSome {
        //     type H <: Ɛ.ElementWrapper[Ɛ.x[ZZ#BASE, AA#BASE], H]
        //   }


/*
        def makeProduct[
          Z <: Ɛ.ELEMENT,
          ZZ <: Ɛ.ElementWrapper[Z, ZZ],
          A <: Ɛ.ELEMENT,
          AA <: Ɛ.ElementWrapper[A, AA]
        ](zxa: Ɛ.x[Z, A], pre : Z => ZZ, star: A => AA): ZZ x AA =
          zxa match {
            case (z, a) =>
              val zz: ZZ = pre(z)
              val aa: AA = star(a)

              // val zxaWrapped: Ɛ.ElementWrapper[Ɛ.x[zz.BASE, aa.BASE]] = Ɛ.ElementWrapper(zxa)
              // zxaWrapped(zz, aa).asInstanceOf[ZZ x AA]

            class ProductElementWrapper[
              Z <: Ɛ.ELEMENT,
              ZZ <: Ɛ.ElementWrapper[Z, ZZ],
              A <: Ɛ.ELEMENT,
              AA <: Ɛ.ElementWrapper[A, AA]
              ] (
                zxa: Ɛ.x[Z, A],
                zz: ZZ,
                aa: AA
              ) extends (ZZ, AA)(zz, aa) with Ɛ.ElementWrapper[
                Ɛ.x[Z, A], 
                ProductElementWrapper[Z, ZZ, A, AA]
              ] {
              override val element = wrapper.element
            }

            // new ProductElementWrapper[Z, ZZ, A, AA](zxa, zz, aa)
            null
          }
*/
      }
    }
  }
}

object UnivariateTypeProjectionHell {
  trait ToposLite { Ɛ =>
    type ELEMENTARY[T]
    type ELEMENT[T <: ELEMENTARY[T]] <: ELEMENTARY[T]
    type *[T <: ELEMENT[T]] <: (T => T) with ELEMENT[*[T]]

    trait ElementWrapper[A <: ELEMENT[A]] { wrapper =>
      type BASE = A
      val element: A

      def apply[F <: ELEMENT[F], G <: ELEMENT[G]](fun: F => G): (F => G) with ElementWrapper[A] =
        new (F => G) with ElementWrapper[A] {
          override val element = wrapper.element
          def apply(f : F): G = fun(f)
        }
    }

    class ActionsLite extends ToposLite {
      type ELEMENTARY[TT] = Ɛ.ElementWrapper[T] forSome {
        type T <: Ɛ.ELEMENT[T]
      }
      type ELEMENT[TT <: ELEMENTARY[TT]] = Ɛ.ElementWrapper[TT#BASE] 

      override type *[TT <: ELEMENT[TT]] <: (TT => TT) with ELEMENT[*[TT]] with Ɛ.ElementWrapper[Ɛ.*[TT#BASE]]
      // that's with Ɛ.ElementWrapper[*[TT]#BASE]
      // but we want:
      //        with Ɛ.ElementWrapper[Ɛ.*[TT#BASE]]
      // can have both???!
      // so the double "with" sort of forces *[TT]#BASE ::== Ɛ.*[TT#BASE]

      class Star[
        TT <: ELEMENT[TT]
      ] (
        innerStar: Ɛ.*[TT#BASE]
      ) extends (TT => TT) /* with ELEMENT[*[TT]] */ with Ɛ.ElementWrapper[Ɛ.*[TT#BASE]] {
        override type BASE = Ɛ.*[TT#BASE]
        override val element = innerStar : BASE
        def apply(tt: TT): TT = tt
      }

      // override type *[T <: ELEMENT[T]] <: (T => T) with ELEMENT[TT] forSome {
      //   type TT <: Ɛ.ElementWrapper[Ɛ.*[T#BASE], H]
      // }

      // class MindFuck[T <: ELEMENT](tStar: Ɛ.*[T#BASE]) extends (T => T) with 
      //   Ɛ.ElementWrapper[Ɛ.*[T#BASE], MindFuck[T]] {

      //   }

      // def star[T <: ELEMENT](tStar: Ɛ.*[T#BASE]): *[T] = {
      //   val mf: (T => T) with  = new MindFuck[T](t)
      //   mf
      // }
    }
  }
}

object UnivariateTypeProjectionHell2 {
  trait ToposLite { Ɛ =>
    type ELEMENTARY[+T]
    type ELEMENT[+T <: ELEMENTARY[T]] <: ELEMENTARY[T]
    type *[T <: ELEMENT[T]] <: (() => T) with ELEMENT[*[T]]

    trait ElementWrapper[A <: ELEMENT[A]] { wrapper =>
      type BASE = A
      val element: A

      def apply[F <: ELEMENT[F], G <: ELEMENT[G]](fun: F => G): (F => G) with ElementWrapper[A] =
        new (F => G) with ElementWrapper[A] {
          override val element = wrapper.element
          def apply(f : F): G = fun(f)
        }
    }

    class ActionsLite extends ToposLite {
      override type ELEMENTARY[+TT] = Ɛ.ElementWrapper[T] forSome {
        type T <: Ɛ.ELEMENT[T]
      }
      override type ELEMENT[+TT <: ELEMENTARY[TT]] = Ɛ.ElementWrapper[TT#BASE] 

      override type *[TT <: ELEMENT[TT]] <: (() => TT) with ELEMENT[*[TT]] // with Ɛ.ElementWrapper[Ɛ.*[TT#BASE]]

// ELEMENT[*[TT]] ::== Ɛ.ElementWrapper[*[TT]#BASE]
// but: *[TT] <: 
//
// ...
// To have Star[TT] <: *[TT], we need:
// Star[TT] <: (() => TT) with ELEMENT[Star[TT]]
// and rhsrhs is
// Ɛ.ElementWrapper[Star[TT]#BASE]
// so, need:
// Star[TT] < (() => TT) with Ɛ.ElementWrapper[Star[TT]#BASE]

      class Star1[
        TT <: ELEMENT[TT]
      ] (
        tt: TT,
        innerStar: Ɛ.*[TT#BASE]
      ) extends (() => TT) /* with ELEMENT[*[TT]] */ with Ɛ.ElementWrapper[Ɛ.*[TT#BASE]] {
        override type BASE = Ɛ.*[TT#BASE]
        override val element = innerStar : BASE
        def apply(): TT = tt
      }

      
      // trait Star2Base1[
      //   TT <: ELEMENT[TT],
      //   TTT
      // ] { self: Ɛ.ELEMENTARY[TTT] => 
      // }

      // trait Star2Base2[
      //   TT <: ELEMENT[TT]
      // ] extends Star2Base1[TT, Star2Base2[TT]] { self: Ɛ.ELEMENT[Star2Base2[TT]] =>
      // }

      // class Star2[
      //   TT <: ELEMENT[TT]
      // ] (
      //   tt: TT,
      //   starBase : Star2Base[TT]
      // ) extends (() => TT) with Ɛ.ElementWrapper[Star2Base[TT]] {
      //   override type BASE = Star2Base[TT]
      //   override val element = starBase : Star2Base[TT]
      //   def apply(): TT = tt
      // }

    }
  }
}          

object UnivariateTypeProjectionHell3 {
  trait ToposLite { Ɛ =>
    type ELEMENTARY[T]
    type ELEMENT[T <: ELEMENTARY[T]] <: ELEMENTARY[T]
    type *[T <: ELEMENT[T]] <: (() => T) with ELEMENT[*[T]]

    trait ElementWrapper[A <: ELEMENT[A]] { wrapper =>
      type BASE = A
      val element: A

      def apply[F <: ELEMENT[F], G <: ELEMENT[G]](fun: F => G): (F => G) with ElementWrapper[A] =
        new (F => G) with ElementWrapper[A] {
          override val element = wrapper.element
          def apply(f : F): G = fun(f)
        }
    }

    class ActionsLite extends ToposLite {
      type ELEMENTARY[TT] = Ɛ.ElementWrapper[T] forSome {
        type T <: Ɛ.ELEMENT[T]
      }
      type ELEMENT[TT <: ELEMENTARY[TT]] = Ɛ.ElementWrapper[TT#BASE] 

      private type **[TT <: ELEMENT[TT], H <: ELEMENTARY[H]] = (() => TT) with ELEMENT[H] 
      override type *[TT <: ELEMENT[TT]] = H forSome { type H <: **[TT, H] }

      class Star[
        TT <: ELEMENT[TT],
        B <: Ɛ.ELEMENT[B]
      ](
        tt: TT,
        b: B
      ) extends (() => TT) with Ɛ.ElementWrapper[B] {
        override val element = b
        override def apply(): TT = tt
      }

      class StarBase[TT <: ELEMENT[TT]](
        tt: TT
      )

      class StarStar[TT <: ELEMENT[TT]](
        tt: TT
      ) extends (() => TT) { // with Ɛ.ElementWrapper[StarBase[TT]] { self: **[TT, StarStar[TT]] =>
        override def apply(): TT = tt
        // override val element = /* tt.element */ null.asInstanceOf[StarBase[TT]]
      }

      // def star[
      //   TT <: ELEMENT[TT]
      // ](
      //   tt: TT
      // ): *[TT] =
      //   new StarStar[TT](tt)

    }
  }
}



