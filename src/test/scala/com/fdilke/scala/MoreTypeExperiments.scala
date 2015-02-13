package com.fdilke.scala

import com.fdilke.scala.MoreTypeExperiments.TypeProjectionHell.Ɛ
import com.fdilke.scala.MoreTypeExperiments.TypeProjectionHell.Ɛ.ElementWrapper
import org.scalatest.FunSpec
import scala.language.higherKinds

// User: Felix Date: 29/05/2014 Time: 19:18

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

    val foo : T#DOT[FOO]
    val bar : T#DOT[BAR]

    val foo2bar : T#ARROW[FOO, BAR]
  }

  abstract class SimplifiedToposTests[
      T <: Topos,
      F <: ToposFixtures[T]
    ](topos: T, fixtures: F) extends FunSpec { self =>

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

        def apply[F, G](f: F, g: G): (F, G) with ElementWrapper[A, AA] =
          new (F, G)(f, g) with ElementWrapper[A, AA] {
            override val element = wrapper.element
          }

        def apply[F, G](f2g: F => G): (F => G) with ElementWrapper[A, AA] =
          new (F => G) with ElementWrapper[A, AA] {
            def apply(f: F): G = f2g(f)

            override val element = wrapper.element
          }

        def humdrum(aa: AA, starA: Ɛ.*[A]): (AA, AA) with Ɛ.ElementWrapper[Ɛ.*[BASE], H] forSome {
            type H <: Ɛ.ElementWrapper[Ɛ.*[BASE], H]
        } = {
          class RecursiveStar[A <: ELEMENT](starA: Ɛ.*[A]) extends (AA, AA)(aa, aa) with ElementWrapper[Ɛ.*[A], RecursiveStar[A]] {
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

//      type *![SS <: ELEMENT] = **[_, SS, _]

//      type KKK[T[M]] = { type λ = Array[T] }#λ

//      type *![SS <: ELEMENT] = **[SS#BASE, SS, H] forSome {
////        type S <: Ɛ.ELEMENT
//        type H <: Ɛ.ElementWrapper[Ɛ.*[SS#BASE], H]
//      }
      override type *[SS <: ELEMENT] = (SS, SS) with ELEMENT

//      type ***[
//        SS <: Ɛ.ElementWrapper[_, SS],
//        H <: Ɛ.ElementWrapper[Ɛ.*[SS#BASE], H]
//      ] = (SS, SS) with Ɛ.ElementWrapper[Ɛ.*[SS#BASE], H]

      type **[
        S <: Ɛ.ELEMENT,
        SS <: Ɛ.ElementWrapper[S, SS],
        H <: Ɛ.ElementWrapper[Ɛ.*[SS#BASE], H]
      ] = (SS, SS) with Ɛ.ElementWrapper[Ɛ.*[S], H]

      def onAPlate[
        S <: Ɛ.ELEMENT,
        SS <: Ɛ.ElementWrapper[S, SS],
        H <: Ɛ.ElementWrapper[Ɛ.*[S], H]
      ] (
        star: (SS, SS) with Ɛ.ElementWrapper[Ɛ.*[S], H]
      ) : **[S, SS, H] = star

      def `try*`[
        Z <: Ɛ.ELEMENT, ZZ <: Ɛ.ElementWrapper[Z, ZZ]
      ](zz: ZZ, starZ: Ɛ.*[Z]): **[Z, ZZ, _] = {
        zz.humdrum(zz, starZ)
      }
    }
  }
}