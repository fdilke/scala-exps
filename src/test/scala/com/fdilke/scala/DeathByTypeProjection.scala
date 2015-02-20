package com.fdilke.scala

import org.scalatest.FunSpec
import scala.language.higherKinds

object DeathByTypeProjection {
	trait ToposLite { Ɛ =>
		type ~[+T]
		type *[+T <: ~[T], S <: ~[S]] <: (() => T) with ~[*[T, S]]

		class MonoidsLite extends ToposLite {
			trait ~~[T <: Ɛ.~[T]] {
				type BASE = T
				type STAR[TT <: ~[TT]] = H forSome {
					type H <: (() => TT) with ~~[Ɛ.*[T, H]] with ~[H]
				}
			}

			type ~[+TT] = TT forSome {
				type T <: Ɛ.~[T]
				type TT <: ~~[T]
			}

			override type *[+TT <: ~[TT], S <: ~[S]] = TT#STAR[TT] with ~~[Ɛ.*[TT#BASE, S#BASE]]

			class Widget[T <: Ɛ.~[T], TT <: ~~[T]]

			// def widget[T <: Ɛ.~[T], TT <: ~~[T]]() =
			// 	new Widget[Ɛ.*[T, X], *[TT, Y]]

			// Type equation: 
			// for T <: Ɛ.~[T], TT <: ~~[T] seek *[TT] <: ~~[Ɛ.*[T, X]]

			// seek *[TT, Y] <: ~~[Ɛ.*[T, X]]
			// lhs is SS#STAR[TT]
		}
	}
}