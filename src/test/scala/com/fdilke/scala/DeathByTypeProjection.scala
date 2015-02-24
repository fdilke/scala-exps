package com.fdilke.scala

import org.scalatest.FunSpec
import scala.language.higherKinds

object DeathByTypeProjection {
	trait ToposLite { Ɛ =>
		type ~[T]
		type *[T <: ~[T], S] <: (() => T) with ~[S]

		class MonoidsLite extends ToposLite {
			trait ~~[T <: Ɛ.~[T], TT <: ~~[T, TT]] {
				type BASE = T
				type STAR[TTT, S] <: (() => TTT) with ~[S]
			}

			type ~[TT] = TT forSome {
				type T <: Ɛ.~[T]
				type TT <: ~~[T, TT]
			}

			override type *[TT <: ~[TT], S] = TT#STAR[TT, S]

			trait Widget[T <: Ɛ.~[T], TT <: ~~[T, TT]]

			// def widget[T <: Ɛ.~[T], TT <: ~~[T]]() =
			// 	new Widget[Ɛ.*[T, X], *[TT, Y]]

			// Need a class extending ~~...
		}
	}
}

object PossibleSolutionByTypeProjectionWithBiproducts {
	trait ToposLite { Ɛ =>
		type ~
		type x[S <: ~, T <: ~] <: (S, T) with ~

		class ActionsLite extends ToposLite {
			trait ~~[T <: Ɛ.~] {
				type BASE = T
			}

			type ~ = ~~[T] forSome { type T <: Ɛ.~ }

			// override type x[SS <: ~, TT <: ~] = (SS, TT) with ~~[Ɛ.x[SS#BASE, TT#BASE]]
			override type x[SS <: ~, TT <: ~] = Helper[SS, TT]#BIP

			class Widget[T <: Ɛ.~, TT <: ~~[T]]

			class Helper[SS <: ~, TT <: ~] {
				type EX = Ɛ.x[SS#BASE, TT#BASE]
				type BIP = (SS, TT) with ~~[EX]

				def check(bip: BIP): SS x TT = bip
				def check2(ssXtt: SS x TT): BIP = ssXtt

				type WIDGET = Widget[EX, BIP]

				def makeWidget = new Widget[EX, BIP]
			}

			// def widget[S <: Ɛ.~, SS <: ~~[S], T <: Ɛ.~, TT <: ~~[T]]() =
			// 	new Widget[Ɛ.x[SS#BASE, TT#BASE], x[SS, TT]]
			def widget[S <: Ɛ.~, SS <: ~~[S], T <: Ɛ.~, TT <: ~~[T]]() =
				new Helper[SS, TT].makeWidget
		}
	}
}

/*

x[S, T] = (S, T) with ~
and the bright idea was to tighten this up to:
x[SS, TT] = (SS, TT) with ~~[Ɛ.x[SS#BASE, TT#BASE]]
because then... Why did that seem like a good idea?

Because: when you multiply an ss = Widget[S, SS] and a tt = Widget[T, TT]
the result should be a Widget[S x T, xx] which is an S x T appropriately dressed up
So we need a type xx which is an appropriate extension of Ɛ.x[S x T]

Constraint is:
	ActionStar[Z, ZZ] x ActionStar[A, AA] has to be an ActionStar[Ɛ.x[ZZ#BASE, AA#BASE], ZZ x AA]

so: ZZ x AA = (ZZ, AA) with ~~[Ɛ.x[ZZ#BASE, AA#BASE]]
Why did we tighten? It used to be just:
    ZZ x AA = (ZZ, AA) with ~~[...]
	but then ZZ x AA and Z x A weren't properly interchangeable. 
	We needed to know that the lhs wrapped the rhs - not just by convention.

So under 'tightening' we have:
 override type x[SS <: ELEMENT, TT <: ELEMENT] = (SS, TT) with Ɛ.ElementWrapper[Ɛ.x[SS#BASE, TT#BASE]]
 RightActionStar[A <: Ɛ.ELEMENT, AA <: Ɛ.ElementWrapper[A]]
 and now we want to construct a
 RightActionStar[Ɛ.x[ZZ#BASE, AA#BASE], ZZ x AA]

 and we can't. 

 The following type constraints need to be satisfied:
 Ɛ.x[ZZ#BASE, AA#BASE] <: Ɛ.ELEMENT
 ZZ x AA <: Ɛ.ElementWrapper[Ɛ.x[ZZ#BASE, AA#BASE]]

 Looks like they are to me. What's the problem????
*/

object CompilerBeingSillyAboutUnit {
	trait ToposLite { Ɛ =>
		type ~
		type UNIT <: ~

		class ActionsLite extends ToposLite {
			trait ~~[T <: Ɛ.~, TT <: ~~[T, TT]] {
				val element: T
			}

	      	override type ~ = TT forSome {
		        // type T <: Ɛ.~
		        // type TT <: ~~[T, TT]
		        type TT <: ~~[_ <: Ɛ.~, TT]
		     }

			class VanillaWrapper[A <: Ɛ.~](a: A) extends
		    	~~[A, VanillaWrapper[A]] {
		        override val element = a
		  	}

			override type UNIT = VanillaWrapper[Ɛ.UNIT]
		}
	}
}

object CompilerBeingSillyAboutBiproducts {
	trait ToposLite { Ɛ =>
		type ~
		type x[S <: ~, T <: ~] <: (S, T) with ~

		class ActionsLite extends ToposLite {
			trait ~~[T <: Ɛ.~, TT <: ~~[T, TT]] {
				val element: T

				type PREBIPRODUCT[SS <: ~] = SS#POSTBIPRODUCT[T, TT] 
				type POSTBIPRODUCT[U <: Ɛ.~, UU <: ~~[T, TT]] = H forSome {
					type H <: ~~[Ɛ.x[T, U], H]
				}
			}

	      	override type ~ = TT forSome {
		        type TT <: ~~[_ <: Ɛ.~, TT]
		     }

		    override type x[SS <: ~, TT <: ~] = (SS, TT) with TT#PREBIPRODUCT[SS]

			class VanillaWrapper[A <: Ɛ.~](a: A) extends
		    	~~[A, VanillaWrapper[A]] {
		        override val element = a
		  	}

		}
	}
}

object AvoidingRevealAndReceive {
	type ~
	trait ~~[T <: ~, TT <: ~~[T, TT]] 

  	type Doubleton = TT forSome {
        type TT <: ~~[_ <: ~, TT]
     }

	trait InnerStar[TT <: Doubleton] {
		def preProduct[S <: ~, SS <: ~~[S, SS]](pre: Star[S, SS]): BiproductStar[SS, TT]
	}

	trait BiproductStar[SS <: Doubleton, TT <: Doubleton] 

	trait Star[T <: ~, TT <: ~~[T, TT]] extends InnerStar[TT] {
		def product[U <: ~, UU <: ~~[U, UU]](that: Star[U, UU]): BiproductStar[TT, UU]

		def preProduct[S <: ~, SS <: ~~[S, SS]](pre: Star[S, SS]): BiproductStar[SS, TT] =
			pre.product(this)

		def product[UU <: Doubleton](that: InnerStar[UU]): BiproductStar[TT, UU] =
			that.preProduct(this)
	}
}

object CompilerStillBeingSillyAboutBiproducts {
	trait ToposLite { Ɛ =>
		type ~
		type x[S <: ~, T <: ~] <: (S, T) with ~
		type STAR[S <: ~]
		type BIPRODUCT[L <: ~, R <: ~] = BiproductStar[L, R] with STAR[L x R]
		trait BiproductStar[L <: ~, R <: ~] { star: STAR[L x R] =>
		}

		class ActionsLite extends ToposLite {
			trait ~~[T <: Ɛ.~, TT <: ~~[T, TT]] {
				val element: T

				// def preBiproduct[SS <: ~](bogusSS: SS, bogusTT: TT): BIPRODUCT[SS, TT] = 
				// 	null
					// bogusSS.postBiproduct[T, TT](bogusTT, bogusSS)

				// def postBiproduct[U <: Ɛ.~, UU <: ~~[U, UU]](bogusTT: TT, bogusUU: UU): BIPRODUCT[TT, UU] =
				// 	null

					// new Star[Ɛ.x[T, U], x[TT, UU]] with BiproductStar[TT, UU] {
					// 	def ^(sXt: Ɛ.x[T, U]): x[TT, UU] = null
					// }

					// new Star[Ɛ.x[S, T], x[SS, TT]] with BiproductStar[SS, TT] {
					// 	def ^(sXt: Ɛ.x[S, T]): x[SS, TT] = null
					// }

					// new Star[S <: Ɛ.~, SS <: ~~[S, SS]r[]
			}

			class BiproductWrapper[
		        A <: Ɛ.~,
		        AA <: ~~[A, AA],
		        B <: Ɛ.~,
		    	BB <: ~~[B, BB]
		    ] (
		        aa: AA,
		        bb: BB,
		        aXb: Ɛ.x[A, B]
		    ) extends (AA, BB)(aa, bb) with ~~[
		        Ɛ.x[A, B],
		        BiproductWrapper[A, AA, B, BB]
		    ] {
		        override val element = aXb
		    }

	      	override type ~ = TT forSome {
		        type TT <: ~~[_ <: Ɛ.~, TT]
		    }

			trait DoubleLinkContextFacade[SS <: ~, TT <: ~] {
				type LINKBIPRODUCT <: (SS, TT) with ~ 
				// type PREBIPRODUCT[SS <: ~] = SS#POSTBIPRODUCT[T, TT] 
				// type POSTBIPRODUCT[U <: Ɛ.~, UU <: ~~[U, UU]] = BiproductWrapper[T, TT, U, UU]
			}

			class DoubleLinkContext[
				S <: Ɛ.~,
				SS <: ~~[S, SS],
				T <: Ɛ.~,
				TT <: ~~[T, TT]
			](left: Star[S, SS], right: Star[T, TT]) extends DoubleLinkContextFacade[SS, TT] {
				override type LINKBIPRODUCT = BiproductWrapper[S, SS, T, TT]

				lazy val biproduct: BIPRODUCT[SS, TT] =
					null
					// new Star[Ɛ.x[S, T], x[SS, TT] /* LINKBIPRODUCT */] with BiproductStar[SS, TT] {
					// 	def ^(sXt: Ɛ.x[S, T]): x[SS, TT] = null.asInstanceOf[x[SS, TT]]
					// }.asInstanceOf[BIPRODUCT[SS, TT]]
			}

		    override type x[SS <: ~, TT <: ~] = DoubleLinkContextFacade[SS, TT]#LINKBIPRODUCT
			type STAR[S <: ~] = StarFacade[S]

			class VanillaWrapper[A <: Ɛ.~](a: A) extends
		    	~~[A, VanillaWrapper[A]] {
		        override val element = a
		  	}

		  	trait StarFacade[SS <: ~]
		  	trait Star[S <: Ɛ.~, SS <: ~~[S, SS]] extends StarFacade[SS] {
		  		def ^(s: S) : SS
		  		def biproduct[T <: Ɛ.~, TT <: ~~[T, TT]](that: Star[T, TT]): BIPRODUCT[SS, TT] = {
		  			// val bogusSS = this.^(null.asInstanceOf[S])
		  			// val bogusTT = that.^(null.asInstanceOf[T])
		  			// bogusTT.preBiproduct(bogusSS, bogusTT)
		  			new DoubleLinkContext[S, SS, T, TT](this, that).biproduct
		  		}
		  	}
		}
	}
}



